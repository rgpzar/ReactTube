package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.dto.UploadedByDto;
import com.ReactTube.backApplication.dto.VideoDto;
import com.ReactTube.backApplication.dto.VideoInputDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoAlreadyExistsException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoNotFoundException;

import com.ReactTube.backApplication.mappers.VideoMapper;
import com.ReactTube.backApplication.mappers.UserMapper;
import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.repositories.VideoRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {
    private final VideoRepo videoRepo;
    private  VisitService visitService;
    private final CommentService commentService;

    private final VideoFileService videoFileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoService.class);

    public VideoService(
            @Autowired VideoRepo videoRepo,
            @Autowired VisitService visitService,
            @Autowired CommentService commentService,
            @Autowired VideoFileService videoFileService
    ) {
        this.videoRepo = videoRepo;
        this.visitService = visitService;
        this.commentService = commentService;
        this.videoFileService = videoFileService;
    }


    public List<VideoDto> getAllVideos(){
        List<Video> videoList = (ArrayList<Video>) videoRepo.findAll();

        return videoList.stream()
                .map(video -> getVideoDtoById(video.getId()))
                .collect(Collectors.toList());
    }

    public List<VideoDto> searchVideos(String searchQuery){
        List<Video> videoList = (ArrayList<Video>) videoRepo.findAll();

        return videoList.stream()
                .filter(video -> video.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))
                .map(video -> getVideoDtoById(video.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public VideoDto getVideoDtoById(long id) {
        Video video = videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
        List<Comment> comments = commentService.getCommentByVideoId(id);
        Collections.reverse(comments);
        Set<Visit> visits = visitService.getVisitsByVideoId(id);

        UploadedByDto uploadedBy = UserMapper.INSTANCE.uploadedByDtoFromUser(video.getUploadedBy());

        return new VideoDto(video, (ArrayList<Comment>) comments, visits, uploadedBy);
    }

    @Transactional
    public Video getVideoById(long id) {
        return videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
    }

    public Boolean existsByTitle(String title){
        return videoRepo.existsByTitle(title);
    }

    public Boolean saveVideo(Video video){
        boolean videoExists = existsByTitle(video.getTitle());

        if(videoExists){
            throw new VideoAlreadyExistsException("Video already exists.");
        }

        try{
            video.setUploadDate(new Date());
            videoRepo.save(video);
            return true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return false;
    }

    public boolean deleteVideo(long videoId){
        try{
            videoRepo.deleteById(videoId);
            return true;
        }catch (Exception e){
            System.out.println(e); //log e
            return false;
        }

    }

    public void updateVideo(Video video, VideoInputDto videoInputDto) throws IOException {
        videoFileService.updateVideoTitle(video.getTitle(), videoInputDto.getTitle());
        VideoMapper.INSTANCE.updateVideoFromVideoInputDto(videoInputDto, video);
        videoRepo.save(video);
    }
}
