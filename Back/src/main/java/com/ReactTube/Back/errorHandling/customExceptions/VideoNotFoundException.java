package com.ReactTube.Back.errorHandling.customExceptions;

public class VideoNotFoundException extends ResourceNotFoundException{
    public VideoNotFoundException(String message){
        super(message);
    }
}
