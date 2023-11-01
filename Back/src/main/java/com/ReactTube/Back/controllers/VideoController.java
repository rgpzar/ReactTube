package com.ReactTube.Back.controllers;


import com.ReactTube.Back.dto.VideoDto;
import com.ReactTube.Back.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.Back.models.Comment;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.models.Visit;
import com.ReactTube.Back.services.CommentService;
import com.ReactTube.Back.services.VideoService;
import com.ReactTube.Back.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private CommentService commentService;

    @GetMapping()
    public ArrayList<Video> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoDto getVideoById(@PathVariable("id") long id) throws NoUserAuthorizedException {
        return videoService.getVideoById(id);
    }

    @PostMapping()
    public Video saveVideo(@RequestBody Video video){
        return videoService.saveVideo(video);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteVideo(@PathVariable("id") long id){
        return videoService.deleteVideo(id);
    }

    @PostMapping("/{id}/addComment")
    public Comment addCommment(@PathVariable("id") long videoId, @RequestBody String comment) throws NoUserAuthorizedException {
        return commentService.addComment(videoId, comment);
    }

    @PutMapping("/{id}/like")
    public Visit updateLike(@PathVariable("id") long videoId, @RequestParam Boolean like) throws NoUserAuthorizedException {
        return visitService.updateLike(videoId, like);
    }
}
