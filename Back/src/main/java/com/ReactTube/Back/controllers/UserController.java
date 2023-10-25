package com.ReactTube.Back.controllers;

import com.ReactTube.Back.models.User;
import com.ReactTube.Back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ArrayList<User> getUsers(){
        return userService.getUsers();
    }
    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping("/search")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Boolean userExistsByEmail(@RequestParam String email){
        return userService.userExistsByEmail(email);
    }

    public Boolean userExistsByUsername(@RequestParam String username){
        return userService.userExistsByUsername(username);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @GetMapping(path = "/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Optional<User> getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @PostMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @PutMapping(path = "/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public User updateUser(@RequestBody User user, @PathVariable("id") Long id){
        return userService.updateUser(user, id);
    }

    @PreAuthorize("hasAuthority('ROLE_ ADMIN')")
    @DeleteMapping(path = "/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public Boolean deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }
}
