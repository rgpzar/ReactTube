package com.ReactTube.Back.errorHandling.customExceptions;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException(String message){
        super(message);
    }
}
