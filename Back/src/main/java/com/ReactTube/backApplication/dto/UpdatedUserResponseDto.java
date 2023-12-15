package com.ReactTube.backApplication.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdatedUserResponseDto {
    private UserOutputDto userOutputDto;
    private String jwt;
}
