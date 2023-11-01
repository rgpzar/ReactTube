package com.ReactTube.Back.errorHandling.customExceptions;

public class VisitNotFoundException extends ResourceNotFoundException{
    public VisitNotFoundException(String message){
        super(message);
    }
}
