package com.ReactTube.backApplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "uploadedBy", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User uploadedBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String uploadDate;

    private String description;
}
