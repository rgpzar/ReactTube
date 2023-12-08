package com.ReactTube.backApplication.controllers;

import com.ReactTube.backApplication.dto.VideoDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

@RestController
@RequestMapping("/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VideoController {
    private final VideoService videoService;
    private final VisitService visitService;
    private final CommentService commentService;
    private final VideoFileService videoFileService;

    private final AuthenticationService authenticationService;

    private final Logger LOGGER = Logger.getLogger(VideoController.class.getName());

    public VideoController(
            @Autowired VideoService videoService,
            @Autowired VisitService visitService,
            @Autowired CommentService commentService,
            @Autowired VideoFileService videoFileService,
            @Autowired AuthenticationService authenticationService
    ) {
        this.videoService = videoService;
        this.visitService = visitService;
        this.commentService = commentService;
        this.videoFileService = videoFileService;
        this.authenticationService = authenticationService;
    }

    @GetMapping()
    public List<VideoDto> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoDto getVideoById(@PathVariable("id") long id) {
        return videoService.getVideoDtoById(id);
    }

    @GetMapping("/search")
    public List<VideoDto> searchVideos(@RequestParam String searchQuery){
        return videoService.searchVideos(searchQuery);
    }

    @GetMapping(value = "watch/{id}", produces = "video/mp4")
    public Mono<Resource> watchVideo(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        return videoFileService.getVideo(video);
    }

    @PostMapping("/upload")
    public String uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try{
            //Use getCurrentAuthenticatedUser() to get the current logged user, there will be a logged user because of the security
            User currentUser = authenticationService.getCurrentAuthenticatedUser();



            if(!videoService.existsByTitle(title)){
                videoFileService.uploadVideo(file, title);

                Video video = Video.builder()
                        .title(title)
                        .description(description)
                        .uploadedBy(currentUser)
                        .uploadDate(formatter.format(new Date()))
                        .durationInSeconds(videoFileService.getVideoDurationInSeconds(title))
                        .build();
                videoService.saveVideo(video);
            }else {
                return "Video already exists";
            }

            return "Video uploaded successfully";
        } catch (Exception e){
            LOGGER.warning(e.getMessage());
            return e.getMessage();
        }
    }

    @GetMapping(value = "getVideoThumbnail/{id}", produces = "image/png")
    public Mono<Resource> getVideoThumbnail(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        return videoFileService.getVideoThumbnail(video.getTitle());
    }

    @PostMapping()
    public Boolean saveVideo(@RequestBody Video video){
        return videoService.saveVideo(video);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteVideo(@PathVariable("id") long id) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(id);
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if(video.getUploadedBy().getId() != authenticatedUser.getId()){
            throw new NoUserAuthorizedException("User is not authorized to delete this video");
        }

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