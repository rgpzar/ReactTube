package com.ReactTube.backApplication;

import com.ReactTube.backApplication.models.Role;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.services.UserService;
import com.ReactTube.backApplication.services.VideoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataLoader {
    private final UserService userService;
    private final VideoService videoService;

    InitialDataLoader(UserService userService, VideoService videoService){
        this.userService = userService;
        this.videoService = videoService;
    }
    public void insertInitialData(){
        List<Object> userList = Arrays.asList(
                User.builder()
                        .username("rgrapac")
                        .email("rgranadospacheco@gmail.com")
                        .password("Test123")
                        .role(Role.ADMIN)
                        .build(),
                User.builder()
                        .username("Guest")
                        .email("Guest")
                        .password("")
                        .role(Role.USER)
                        .build(),
                Video.builder()
                        .title("untitled2")
                        .description("Music Video 1")
                        .uploadDate("1/12/2023")
                        .uploadedBy(userService.getUserById(23L).get())
                        .build(),
                Video.builder()
                        .title("FornitePerverso")
                        .description("Music Video 2")
                        .uploadDate("1/12/2023")
                        .uploadedBy(userService.getUserById(23L).get())
                        .build()
        );

        for(Object o: userList){
            try{
                if(o instanceof User){
                    userService.saveUser((User) o);
                } else if (o instanceof Video) {
                    videoService.saveVideo((Video) o);
                }


            }catch (Exception e){
                System.out.printf("Data %s already loaded %n", o);
            }
        }
    }
}
