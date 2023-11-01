package com.ReactTube.Back.services;

import com.ReactTube.Back.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.Back.errorHandling.customExceptions.ResourceNotFoundException;
import com.ReactTube.Back.errorHandling.customExceptions.VideoNotFoundException;
import com.ReactTube.Back.models.Comment;
import com.ReactTube.Back.models.CompositeKeys.UserVideoPK;
import com.ReactTube.Back.models.User;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.repositories.CommentRepo;
import com.ReactTube.Back.repositories.UserRepo;
import com.ReactTube.Back.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationService authenticationService;

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
        newComment.setComment(commentString);

        return commentRepo.save(newComment);
    }

    public List<Comment> getCommentByVideoId(long videoId){
        return commentRepo.findByIdVideoId(videoId);
    }
}
