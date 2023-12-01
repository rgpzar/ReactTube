package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.models.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Builder
@Data
@AllArgsConstructor
public class StreamService {
    private static final String FORMAT = "classpath:videos/%s.mp4";
    private final ResourceLoader resourceLoader;
    private final VisitService visitService;

    public Mono<Resource> getVideo(Video video) throws NoUserAuthorizedException {
        visitService.addVisit(video);
        return Mono.fromSupplier(
                () -> resourceLoader.getResource(String.format(FORMAT, video.getTitle()))
        );
    }
}
