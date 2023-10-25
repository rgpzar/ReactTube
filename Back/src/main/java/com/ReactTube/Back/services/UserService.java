package com.ReactTube.Back.services;

import com.ReactTube.Back.models.User;
import com.ReactTube.Back.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) userRepo.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepo.findById(id);
    }

    public Boolean userExistsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public Boolean userExistsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    public User updateUser(User user, Long id){
        user.setId(id);
        return userRepo.save(user);
    }

    public Boolean deleteUser(Long id){
        try {
            userRepo.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /*
    * TO DO:
    *   -Code authUser() function
    * */
}
