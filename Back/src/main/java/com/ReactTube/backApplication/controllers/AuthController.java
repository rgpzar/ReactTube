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

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Builder
@Data
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    @PreAuthorize("permitAll")
    @PostMapping("/login")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authRequest, HttpServletRequest request) {
        AuthenticationResponse jwt = null;
        try{
            jwt = authService.login(authRequest, request);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("A not authorized client tried to access into the API");
        }finally {
            System.out.println(authRequest.getUsername());
            System.out.println(authRequest.getPassword());
        }

        return ResponseEntity.ok(jwt);
    }
}
