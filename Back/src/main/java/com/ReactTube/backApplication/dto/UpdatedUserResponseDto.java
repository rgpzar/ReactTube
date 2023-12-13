package com.ReactTube.backApplication.dto;

import com.ReactTube.backApplication.mappers.UserUpdateMapper;
import com.ReactTube.backApplication.models.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdatedUserResponseDto {
    private UserOutputDto userOutputDto;
    private String jwt;
}
