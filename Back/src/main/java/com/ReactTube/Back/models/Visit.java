package com.ReactTube.Back.models;

import com.ReactTube.Back.models.CompositeKeys.UserVideoPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Visit {
    @EmbeddedId
    private UserVideoPK id;

    @Column(nullable = false)
    private int timesVisited;

    @Column(nullable = false)
    private boolean userLike;

    public UserVideoPK getId() {
        return id;
    }

    public void setId(UserVideoPK id) {
        this.id = id;
    }

    public int getTimesVisited() {
        return timesVisited;
    }

    public void setTimesVisited(int timesVisited) {
        this.timesVisited = timesVisited;
    }

    public boolean getUserLike() {
        return userLike;
    }

    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }
}
