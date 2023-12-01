package com.ReactTube.backApplication.errorHandling.customExceptions;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException(String message){
        super(message);
    }
}
