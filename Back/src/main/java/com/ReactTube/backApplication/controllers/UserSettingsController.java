package com.ReactTube.backApplication.controllers;

import com.ReactTube.backApplication.dto.AuthenticationRequest;
import com.ReactTube.backApplication.dto.UpdatedUserResponseDto;
import com.ReactTube.backApplication.dto.UserDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.mappers.UserUpdateMapper;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.services.AuthenticationService;
import com.ReactTube.backApplication.services.JwtService;
import com.ReactTube.backApplication.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/setting/")
@Builder
@AllArgsConstructor
@Data
public class UserSettingsController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;


    @GetMapping(path = "/info")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Optional<User> info() throws NoUserAuthorizedException {
        long id = authenticationService.getCurrentAuthenticatedUser().getId();
        return userService.getUserById(id);
    }

    @PutMapping(path = "/update")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public UpdatedUserResponseDto updateUser(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws NoUserAuthorizedException {
        User user = authenticationService.getCurrentAuthenticatedUser();
        String remoteAddress =  httpServletRequest.getRemoteAddr();

        UserUpdateMapper.INSTANCE.updateUserFromDto(userDto, user);

        System.out.println(user);

        userService.saveUser(user);

        UpdatedUserResponseDto updatedUserResponse = UpdatedUserResponseDto.builder()
                .user(user)
                .build();

        if(userDto.getPassword() != null){
            String jwt = jwtService.generateToken(user, authenticationService.generateExtraClaims(user, remoteAddress));
            updatedUserResponse.setJwt(jwt);
        }

        return updatedUserResponse;
    }

    @DeleteMapping(path = "/delete")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Boolean deleteUser() throws NoUserAuthorizedException {
        long id = authenticationService.getCurrentAuthenticatedUser().getId();
        return userService.deleteUser(id);
    }
}
