package com.ReactTube.Back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    //This relationship is to storage the users that have visit this video.
    @ManyToMany
    @JoinTable(
            name = "visit",
            joinColumns = @JoinColumn(name = "videoId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<User> visit;

    @ManyToMany
    @JoinTable(
            name = "comment",
            joinColumns = @JoinColumn(name = "videoId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<User> comment;

    @Column(unique = true, nullable = false)
    private String source;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String uploadDate;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
