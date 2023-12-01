package com.ReactTube.backApplication.errorHandling.customExceptions;

public class CommentNotFoundException extends ResourceNotFoundException{
    public CommentNotFoundException(String message){
        super(message);
    }
}
