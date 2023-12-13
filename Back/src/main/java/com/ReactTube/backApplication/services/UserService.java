package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.dto.UserInputDto;
import com.ReactTube.backApplication.dto.UserOutputDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.ResourceNotFoundException;
import com.ReactTube.backApplication.mappers.UserUpdateMapper;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            @Autowired UserRepo userRepo,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepo.findById(id);
    }

    public User getUserByUsername(String username){
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getAnonymousUser() throws NoUserAuthorizedException {
        return userRepo.findByUsername("Guest").orElseThrow(
                () -> new NoUserAuthorizedException("No guest user")
        );
    }

    public Boolean userExistsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public Boolean userExistsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    @Transactional
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    @Transactional
    public Boolean saveUserList(List<User> userList){
        try{
            for(User user: userList){
                saveUser(user);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User updateUser(UserInputDto userInputDto, long id) throws NoUserAuthorizedException {
        User user = getUserById(id).orElseThrow(
                () -> new NoUserAuthorizedException("No user authorized")
        );

        UserUpdateMapper.INSTANCE.updateUserFromDto(userInputDto, user);

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
}
