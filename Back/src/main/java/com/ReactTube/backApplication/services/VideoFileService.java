package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VideoNotFoundException;
import com.ReactTube.backApplication.models.Video;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.mp4parser.IsoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;

@Service

public class VideoFileService {

    private static final String VIDEO_PATH = "classpath:videos/";
    private static final String FORMAT = VIDEO_PATH +  "%s.mp4";
    private final ResourceLoader resourceLoader;
    private final VisitService visitService;


    public VideoFileService(
            @Autowired ResourceLoader resourceLoader,
            @Autowired VisitService visitService
    ) {
        this.resourceLoader = resourceLoader;
        this.visitService = visitService;
    }

    public Mono<Resource> getVideo(Video video) throws NoUserAuthorizedException {
        return Mono.fromSupplier(
                () -> resourceLoader.getResource(String.format(FORMAT, video.getTitle()))
        );
    }

    public double getVideoDurationInSeconds(String title){
        String filename = "target/classes/videos/" + title + ".mp4";
        File videoFile = new File(filename);

        try (IsoFile isoFile = new IsoFile(videoFile)) {
            return (double)
                    isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                    isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
        } catch (IOException e) {
            throw new VideoNotFoundException("Video couldn't be found.");
        }
    }


    public void uploadVideo(MultipartFile file, String title) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        if(file.getOriginalFilename() == null){
            throw new IOException("File has no name");
        }

        if(file.getOriginalFilename().contains(".mp4")){
            storeVideo(file, title);
        } else {
            throw new IOException("File is not a video");
        }
    }

    public void storeVideo(MultipartFile file, String title) throws IOException {
        byte [] videoBytes = file.getBytes();

        Resource resource = resourceLoader.getResource(VIDEO_PATH);

        System.out.println(resource.getURI().getPath());

        Path path = Paths.get(resource.getURI().getPath().substring(1) + title + ".mp4");

        Files.write(path, videoBytes);

        generateVideoThumbnail(new File(path.toString()), title);
    }

    public void generateVideoThumbnail(File file, String title){
        try{
            Picture picture = FrameGrab.getFrameFromFile(file, 1);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", new File("src/main/resources/thumbnails/" + title + ".png"));
            ImageIO.write(bufferedImage, "png", new File("target/classes/thumbnails/" + title + ".png"));
        }catch (IOException | JCodecException e){
            System.out.println("Error generating thumbnail");
        }
    }

    public Mono<Resource> getVideoThumbnail(String title){
        return Mono.fromSupplier(
                () -> resourceLoader.getResource("classpath:thumbnails/" + title + ".png")
        );
    }

}