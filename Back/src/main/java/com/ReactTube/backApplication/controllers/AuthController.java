package com.ReactTube.backApplication.controllers;

import com.ReactTube.backApplication.dto.AuthenticationRequest;
import com.ReactTube.backApplication.dto.AuthenticationResponse;
import com.ReactTube.backApplication.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
@Builder
@Data
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    @PreAuthorize("permitAll")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authRequest, HttpServletRequest request) {
        AuthenticationResponse jwt = null;
        try{
            jwt = authService.login(authRequest, request);
            LOGGER.info("User " + authRequest.getUsername() + " logged in.");
        }catch (Exception e){
            LOGGER.warning(e.getMessage());
            LOGGER.info("A not authenticated user tried to log in.");
        }

        return ResponseEntity.ok(jwt);
    }
}
