package com.ReactTube.Back.services;

import com.ReactTube.Back.dto.VideoDto;
import com.ReactTube.Back.models.Comment;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.models.Visit;
import com.ReactTube.Back.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class VideoService {
    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private VisitService visitService;

    @Autowired
    private CommentService commentService;

    public ArrayList<Video> getAllVideos(){
        return (ArrayList<Video>) videoRepo.findAll();
    }

    public VideoDto getVideoById(long id){
        Video video = videoRepo.findById(id).orElse(null); //throw VideoNotFoundException -> Self-made
        List<Comment> comments = commentService.getCommentByVideoId(id);
        Set<Visit> visits = visitService.getVisitsByVideoId(id);

        visitService.addVisit(video);

        return new VideoDto(video, (ArrayList<Comment>) comments, visits);
    }

    public Video saveVideo(Video video){
        return videoRepo.save(video);
    }

    public boolean deleteVideo(long videoId){
        try{
            videoRepo.deleteById(videoId);
            return true;
        }catch (Exception e){
            System.out.println(e); //log e
        }

        return false;
    }
}
