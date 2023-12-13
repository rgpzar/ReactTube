package com.ReactTube.backApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOutputDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
