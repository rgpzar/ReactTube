package com.ReactTube.backApplication.dto;

import com.ReactTube.backApplication.models.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdatedUserResponseDto {
    private User user;
    private String jwt;
}
