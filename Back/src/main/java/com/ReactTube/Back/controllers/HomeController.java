package com.ReactTube.Back.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {

    @PreAuthorize("permitAll")
    @GetMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public String welcome(){
        File index = new File("C:\\Users\\rgran\\OneDrive\\Escritorio\\Servidor\\AirportApi\\src\\main\\resources\\static\\index.html");
        StringBuilder result = new StringBuilder();

        try{
            Scanner sc = new Scanner(index);

            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
        }catch (FileNotFoundException e){
            return "Error 404";
        }

        return result.toString();
    }
}
