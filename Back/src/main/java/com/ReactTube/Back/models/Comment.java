package com.ReactTube.Back.models;

import com.ReactTube.Back.models.CompositeKeys.UserVideoPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Comment {
    @EmbeddedId
    private UserVideoPK id;

    @Column(nullable = false)
    private String comment;

    public UserVideoPK getId() {
        return id;
    }

    public void setId(UserVideoPK id) {
        this.id = id;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
