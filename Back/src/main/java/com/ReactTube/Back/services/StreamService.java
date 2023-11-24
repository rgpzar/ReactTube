package com.ReactTube.Back.services;

import com.ReactTube.Back.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamService {

    private static final String FORMAT = "classpath:videos/%s.mp4";

    @Autowired
    private VideoService videoService;

    @Autowired
    private ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(Long videoId) throws NoUserAuthorizedException {
        Video video = videoService.getVideoById(videoId).getVideo();

        return Mono.fromSupplier(
                () -> resourceLoader.getResource(String.format(FORMAT, video.getTitle()))
        );
    }
}
