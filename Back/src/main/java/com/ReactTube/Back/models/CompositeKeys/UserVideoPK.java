package com.ReactTube.Back.models.CompositeKeys;

import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public class UserVideoPK {
    private long userId;
    private Long videoId;

    /*
    *
    * */
    private Date time;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
