package com.ReactTube.Back.errorHandling;

import com.ReactTube.Back.errorHandling.customExceptions.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            CommentNotFoundException.class,
            VideoNotFoundException.class,
            VisitNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<String> handleResourceNotFoundException(){
        return new ResponseEntity<>("Status 404: The resource couldn't be found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoUserAuthorizedException.class)
    public ResponseEntity<String> handleNoUserAuthorizedException(){
        return new ResponseEntity<>("Status 403: You have no access to this resource.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleSignatureException(SignatureException ex, WebRequest request) {
        String errorMessage = "Error de autenticación";
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex){
        ResponseEntity<String> response = null;

        switch (ex){
            case AuthenticationException exT:
                response = new ResponseEntity<>("Status 403: You have no access to this resource.", HttpStatus.FORBIDDEN);
                break;
            default:
                response = new ResponseEntity<>("Status 500: Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
