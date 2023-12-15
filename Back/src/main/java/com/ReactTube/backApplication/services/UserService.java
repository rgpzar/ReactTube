package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.dto.UserInputDto;
import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.ResourceNotFoundException;
import com.ReactTube.backApplication.mappers.UserMapper;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    public User updateUser(UserInputDto userInputDto, long id) throws NoUserAuthorizedException {
        User user = getUserById(id).orElseThrow(
                () -> new NoUserAuthorizedException("No user authorized")
        );

        UserMapper.INSTANCE.updateUserFromDto(userInputDto, user);

        validateUser(user);

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

    private void validateUser(User user) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"; //al menos un número, una letra minúscula, una mayúscula y mínimo 8 caracteres
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; //solo letras, números y algunos caracteres especiales, mínimo 3 caracteres, un @ en medio
        String usernameRegex = "^[a-zA-Z0-9._-]{3,}$"; //solo letras, números y algunos caracteres especiales, mínimo 3 caracteres
        String nameRegex = "^[a-zA-Z\\s]{1,}$"; //solo letras y espacios, al menos 1 carácter
        String phoneRegex = "^\\d{9}$"; //solo números, 9 caracteres

        if(user.getUsername().equals("Guest")){
            return;
        }

        if (user.getUsername() == null || !Pattern.matches(usernameRegex, user.getUsername())) {
            throw new IllegalArgumentException("Invalid username format.");
        }
        if (user.getEmail() == null || !Pattern.matches(emailRegex, user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (user.getPassword() == null || !Pattern.matches(passwordRegex, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format.");
        }
        if (user.getFirstName() != null && !Pattern.matches(nameRegex, user.getFirstName())) {
            throw new IllegalArgumentException("Invalid first name format.");
        }
        if (user.getLastName() != null && !Pattern.matches(nameRegex, user.getLastName())) {
            throw new IllegalArgumentException("Invalid last name format.");
        }
        if (user.getPhoneNumber() != null && !Pattern.matches(phoneRegex, user.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

}
