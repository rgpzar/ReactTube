package com.ReactTube.backApplication.errorHandling.customExceptions;


public class VideoAlreadyExistsException extends RuntimeException {
        public VideoAlreadyExistsException(String message) {
                super(message);
        }
}

