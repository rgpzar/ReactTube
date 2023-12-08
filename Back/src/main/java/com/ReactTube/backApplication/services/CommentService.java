package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoNotFoundException;
import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.CompositeKeys.UserVideoPK;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.repositories.CommentRepo;
import com.ReactTube.backApplication.repositories.VideoRepo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final VideoRepo videoRepo;
    private final AuthenticationService authenticationService;


    public CommentService(
            @Autowired CommentRepo commentRepo,
            @Autowired VideoRepo videoRepo,
            @Autowired AuthenticationService authenticationService
    ) {
        this.commentRepo = commentRepo;
        this.videoRepo = videoRepo;
        this.authenticationService = authenticationService;
    }

    public Comment addComment(long videoId, String commentString) throws NoUserAuthorizedException {
        Video video = videoRepo.findById(videoId)
            .orElseThrow(() -> new VideoNotFoundException("Video couldn't be found"));
        User user = authenticationService.getCurrentAuthenticatedUser();

        UserVideoPK commentId = new UserVideoPK();
        Comment newComment = new Comment();

        commentId.setVideoId(video.getId());
        commentId.setUserId(user.getId());
        commentId.setTime(new Date());

        newComment.setId(commentId);
        newComment.setMessage(commentString);

        return commentRepo.save(newComment);
    }

    public List<Comment> getCommentByVideoId(long videoId){
        return commentRepo.findByIdVideoId(videoId);
    }
}
