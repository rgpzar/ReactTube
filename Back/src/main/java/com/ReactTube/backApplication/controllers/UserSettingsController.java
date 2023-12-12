package com.ReactTube.backApplication.controllers;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/setting/")
@AllArgsConstructor
public class UserSettingsController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;


    @GetMapping(path = "/info")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Optional<UserDto> info() throws NoUserAuthorizedException {
        return Optional.ofNullable(
                UserUpdateMapper.INSTANCE.userDtoFromUser(authenticationService.getCurrentAuthenticatedUser()
        ));
    }

    @PutMapping(path = "/update")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PreAuthorize("authenticated")
    public UpdatedUserResponseDto updateUser(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws NoUserAuthorizedException {
        User user = authenticationService.getCurrentAuthenticatedUser();
        String remoteAddress =  httpServletRequest.getRemoteAddr();

        if(userDto.getPassword() == null || userDto.getPassword().equals("")){
            throw new NoUserAuthorizedException("Password is null or empty");
        }


        UserUpdateMapper.INSTANCE.updateUserFromDto(userDto, user);

        System.out.println(user);

        userService.saveUser(user);

        UpdatedUserResponseDto updatedUserResponse = UpdatedUserResponseDto.builder()
                .user(user)
                .build();

        String password = userDto.getPassword();
        User updatedUser = userService.getUserById(user.getId())
                .orElseThrow(() -> new NoUserAuthorizedException("User not found (updating)"));


        authManager.authenticate(new UsernamePasswordAuthenticationToken(updatedUser.getUsername(), password));
        String jwt = jwtService.generateToken(updatedUser, authenticationService.generateExtraClaims(user, remoteAddress));

        updatedUserResponse.setJwt(jwt);

        return updatedUserResponse;
    }

    @DeleteMapping(path = "/delete")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Boolean deleteUser() {
        long id = authenticationService.getCurrentAuthenticatedUser().getId();
        return userService.deleteUser(id);
    }
}
