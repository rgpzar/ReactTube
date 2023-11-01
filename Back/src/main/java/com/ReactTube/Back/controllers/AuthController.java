package com.ReactTube.Back.controllers;

import com.ReactTube.Back.dto.AuthenticationRequest;
import com.ReactTube.Back.dto.AuthenticationResponse;
import com.ReactTube.Back.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    @Autowired
    AuthenticationService authService;
    @PreAuthorize("permitAll")
    @PostMapping("/login")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authRequest) {
        AuthenticationResponse jwt = null;
        try{
            jwt = authService.login(authRequest);
        }catch (Exception e){
            System.out.println("A not authorized client tried to access into the API");
        }finally {
            System.out.println(authRequest.getUsername());
            System.out.println(authRequest.getPassword());
        }

        return ResponseEntity.ok(jwt);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception exception, HttpServletRequest request){

        Map<String, String> apiError = new HashMap<>();
        apiError.put("message",exception.getLocalizedMessage());
        apiError.put("timestamp", new Date().toString());
        apiError.put("url", request.getRequestURL().toString());
        apiError.put("http-method", request.getMethod());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if(exception instanceof AccessDeniedException){
            status = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity.status(status).body(apiError);
    }
}