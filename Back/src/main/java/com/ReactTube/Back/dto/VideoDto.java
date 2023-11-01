package com.ReactTube.Back.dto;

import com.ReactTube.Back.models.Comment;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.models.Visit;

import java.util.ArrayList;
import java.util.Set;

public class VideoDto {
    private Video video;
    private ArrayList<Comment> videoComments;

    private Set<Visit> videoVisits;

    public VideoDto(Video video, ArrayList<Comment> videoComments, Set<Visit> videoVisits) {
        this.video = video;
        this.videoComments = videoComments;
        this.videoVisits = videoVisits;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public ArrayList<Comment> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(ArrayList<Comment> videoComments) {
        this.videoComments = videoComments;
    }

    public Set<Visit> getVideoVisits() {
        return videoVisits;
    }

    public void setVideoVisits(Set<Visit> videoVisits) {
        this.videoVisits = videoVisits;
    }
}
