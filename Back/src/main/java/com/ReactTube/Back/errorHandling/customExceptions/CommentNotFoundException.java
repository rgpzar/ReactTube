package com.ReactTube.Back.errorHandling.customExceptions;

public class CommentNotFoundException extends ResourceNotFoundException{
    public CommentNotFoundException(String message){
        super(message);
    }
}
