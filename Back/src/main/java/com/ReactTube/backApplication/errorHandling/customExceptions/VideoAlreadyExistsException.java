package com.ReactTube.backApplication.services;


public class VideoAlreadyExistsException extends RuntimeException {
        VideoAlreadyExistsException(String s) {
                super(s);
        }
}

