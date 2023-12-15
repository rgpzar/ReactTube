package com.ReactTube.backApplication.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VideoInputDto {
    private String title;
    private String description;
}
