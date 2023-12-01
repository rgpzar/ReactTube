package com.ReactTube.backApplication.controllers;


import com.ReactTube.backApplication.dto.VideoDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.services.CommentService;
import com.ReactTube.backApplication.services.StreamService;
import com.ReactTube.backApplication.services.VideoService;
import com.ReactTube.backApplication.services.VisitService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/video")
@Builder
@Data
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final VisitService visitService;
    private final CommentService commentService;
    private final StreamService streamService;

    @GetMapping()
    public List<VideoDto> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoDto getVideoById(@PathVariable("id") long id) throws NoUserAuthorizedException {
        return videoService.getVideoDtoById(id);
    }

    @GetMapping(value = "watch/{id}", produces = "video/mp4")
    public Mono<Resource> watchVideo(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);

        return streamService.getVideo(video);
    }

    @PostMapping()
    public Boolean saveVideo(@RequestBody Video video){
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
