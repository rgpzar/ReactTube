package com.ReactTube.backApplication.dto;

import com.ReactTube.backApplication.models.Comment;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private Video video;
    private ArrayList<Comment> videoComments;
    private Set<Visit> videoVisits;
    private UploadedByDto uploadedBy;
}
