package com.ReactTube.Back.repositories;

import com.ReactTube.Back.models.Comment;
import com.ReactTube.Back.models.CompositeKeys.UserVideoPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, UserVideoPK> {
    List<Comment> findByIdVideoId(long videoId);
}
