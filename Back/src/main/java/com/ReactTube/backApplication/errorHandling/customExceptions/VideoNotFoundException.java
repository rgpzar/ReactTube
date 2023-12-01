package com.ReactTube.backApplication.errorHandling.customExceptions;

public class VideoNotFoundException extends ResourceNotFoundException{
    public VideoNotFoundException(String message){
        super(message);
    }
}
