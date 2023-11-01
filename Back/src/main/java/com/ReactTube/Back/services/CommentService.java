package com.ReactTube.Back.services;

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

    public Comment addComment(long videoId, String commentString){
        Video video = videoRepo.findById(videoId).orElse(null); //throw VideoNotFoundException -> Self-made
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            User user = userRepo.findByUsername(auth.getName()).orElse(null); //throw UserNotFoundException -> Self-made
            UserVideoPK commentId = new UserVideoPK();
            Comment newComment = new Comment();

            commentId.setVideoId(video.getId());
            commentId.setUserId(user.getId());
            commentId.setTime(new Date());

            newComment.setId(commentId);
            newComment.setComment(commentString);

            return commentRepo.save(newComment);
        }

        return null;
    }

    public List<Comment> getCommentByVideoId(long videoId){
        return commentRepo.findByIdVideoId(videoId);
    }
}
