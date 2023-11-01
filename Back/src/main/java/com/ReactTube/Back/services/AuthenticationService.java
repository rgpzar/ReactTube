package com.ReactTube.Back.services;

import com.ReactTube.Back.dto.AuthenticationRequest;
import com.ReactTube.Back.dto.AuthenticationResponse;
import com.ReactTube.Back.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.Back.errorHandling.customExceptions.UserNotFoundException;
import com.ReactTube.Back.models.User;
import com.ReactTube.Back.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    public User getCurrentAuthenticatedUser() throws NoUserAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new NoUserAuthorizedException("There is not an authenticated user."));
    }

    public AuthenticationResponse login(AuthenticationRequest authRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            authRequest.getUsername(), authRequest.getPassword()
        );
        authManager.authenticate(authToken);

        Optional<User> userQuery = userRepo.findByUsername(authRequest.getUsername());
        User user = userQuery
                .orElseThrow(() -> new UserNotFoundException("The user couldn't be found."));

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt);
    }

    private Map<String, Object> generateExtraClaims(User user){
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }
}
