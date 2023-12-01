package com.ReactTube.backApplication.errorHandling.customExceptions;

public class VisitNotFoundException extends ResourceNotFoundException{
    public VisitNotFoundException(String message){
        super(message);
    }
}
