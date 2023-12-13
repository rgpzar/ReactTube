package com.ReactTube.backApplication.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class UserInputDto extends UserOutputDto{
    private String password;

    UserInputDto(String username, String email, String password, String firstName, String lastName, String phoneNumber){
        super(username, email, firstName, lastName, phoneNumber);
        this.password = password;
    }
}
