package com.ReactTube.backApplication.controllers;

import com.ReactTube.backApplication.dto.UserDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping()
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping("/search")
    public Boolean userExistsByEmail(@RequestParam String email){
        return userService.userExistsByEmail(email);
    }

    public Boolean userExistsByUsername(@RequestParam String username){
        return userService.userExistsByUsername(username);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping(path = "/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @PostMapping()
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @PutMapping(path = "/{id}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable("id") Long id) throws NoUserAuthorizedException {
        return userService.updateUser(userDto, id);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @DeleteMapping(path = "/{id}")
    public Boolean deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }
}
