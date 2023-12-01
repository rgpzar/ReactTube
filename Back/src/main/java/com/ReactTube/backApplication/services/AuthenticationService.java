package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.dto.AuthenticationRequest;
import com.ReactTube.backApplication.dto.AuthenticationResponse;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.UserNotFoundException;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Builder
@Data
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authManager;
    private final UserRepo userRepo;
    private final JwtService jwtService;


    public User getCurrentAuthenticatedUser() throws NoUserAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new NoUserAuthorizedException("There is not an authenticated user."));
    }

    @Transactional
    public AuthenticationResponse login(AuthenticationRequest authRequest, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            authRequest.getUsername(), authRequest.getPassword()
        );
        authManager.authenticate(authToken);

        Optional<User> userQuery = userRepo.findByUsername(authRequest.getUsername());
        User user = userQuery
                .orElseThrow(() -> new UserNotFoundException("The user couldn't be found."));

        String jwt = jwtService.generateToken(user, generateExtraClaims(user, request.getRemoteAddr()));

        return new AuthenticationResponse(jwt);
    }


    public Map<String, Object> generateExtraClaims(User user, String ipAddr){
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("email", user.getEmail());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("ipAddr", ipAddr);

        return extraClaims;
    }
}
