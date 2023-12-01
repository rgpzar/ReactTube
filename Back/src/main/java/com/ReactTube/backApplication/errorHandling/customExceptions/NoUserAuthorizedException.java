package com.ReactTube.backApplication.errorHandling.customExceptions;


import javax.naming.AuthenticationException;

public class NoUserAuthorizedException extends AuthenticationException {
    public NoUserAuthorizedException(String message){
        super(message);
    }
}
