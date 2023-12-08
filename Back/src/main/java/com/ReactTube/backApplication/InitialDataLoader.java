package com.ReactTube.backApplication;

import com.ReactTube.backApplication.models.Role;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.services.UserService;
import com.ReactTube.backApplication.services.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InitialDataLoader {
    private UserService userService;
    private VideoService videoService;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataLoader.class);

    InitialDataLoader(
            @Autowired UserService userService,
            @Autowired VideoService videoService
    ){
        this.userService = userService;
        this.videoService = videoService;
    }

    private static final List<User> USER_LIST = Arrays.asList(
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
                    .build()
    );

    private static final List<Video> VIDEO_LIST = Arrays.asList(
            Video.builder()
                    .title("untitled2")
                    .description("Music Video 1")
                    .uploadDate("1/12/2023")
                    .build(),
            Video.builder()
                    .title("FornitePerverso")
                    .description("Music Video 2")
                    .uploadDate("1/12/2023")
                    .build(),
            Video.builder()
                    .title("hyper")
                    .description("Music Video 3")
                    .uploadDate("1/12/2023")
                    .build(),
            Video.builder()
                    .title("90's SNS")
                    .description("Music Video 4")
                    .uploadDate("1/12/2023")
                    .build(),
            Video.builder()
                    .title("RapSinCorte-I")
                    .description("Music Video 5")
                    .uploadDate("1/12/2023")
                    .build()/*,
            Video.builder()
                    .title("Project_CRB")
                    .description("Music Video 6")
                    .uploadDate("1/12/2023")
                    .build()*/
    );

    private User uploadedBy;

    public void loadUserList(){
        for(User user: USER_LIST){
            try{
                userService.saveUser(user);
            }catch (Exception e){
                LOGGER.error("User {} already loaded", user);
            }
        }
    }

    public void loadVideoList(){
        uploadedBy = userService.getUserByUsername("rgrapac");

        for(Video video: VIDEO_LIST){
            video.setUploadedBy(uploadedBy);
            try{
                videoService.saveVideo(video);
            }catch(Exception e){
                LOGGER.error("Video {} already loaded", video);
            }
        }
    }
}