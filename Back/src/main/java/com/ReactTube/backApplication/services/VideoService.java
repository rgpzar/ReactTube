package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.dto.UploadedByDto;
import com.ReactTube.backApplication.dto.VideoDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoAlreadyExistsException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoNotFoundException;
import com.ReactTube.backApplication.mappers.UserUpdateMapper;
import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoService {
    private final VideoRepo videoRepo;
    private final VisitService visitService;
    private final CommentService commentService;

    public VideoService(
            @Autowired VideoRepo videoRepo,
            @Autowired VisitService visitService,
            @Autowired CommentService commentService
    ) {
        this.videoRepo = videoRepo;
        this.visitService = visitService;
        this.commentService = commentService;
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

    public VideoDto getVideoDtoById(long id) {
        Video video = videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
        List<Comment> comments = commentService.getCommentByVideoId(id);
        Set<Visit> visits = visitService.getVisitsByVideoId(id);

        UploadedByDto uploadedBy = UserUpdateMapper.INSTANCE.uploadedByDtoFromUser(video.getUploadedBy());

        return new VideoDto(video, (ArrayList<Comment>) comments, visits, uploadedBy);
    }

    public Video getVideoById(long id) {
        return videoRepo.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found."));
    }

    public Boolean existsByTitle(String title){
        return videoRepo.existsByTitle(title);
    }

    public Boolean saveVideo(Video video){
        if(existsByTitle(video.getTitle())){
            throw new VideoAlreadyExistsException("Video already exists.");
        }

        try{
            videoRepo.save(video);
            return true;
        }catch (Exception e){
            return false;
        }
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
}
