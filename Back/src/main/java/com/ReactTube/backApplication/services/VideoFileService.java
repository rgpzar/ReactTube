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

    private static final String VIDEO_EXTENSION = ".mp4";
    private static final String THUMBNAIL_EXTENSION = ".png";

    private static final String VIDEO_PATH = "classpath:videos/";
    private static final String FORMAT = VIDEO_PATH +  "%s" + VIDEO_EXTENSION;
    private final ResourceLoader resourceLoader;
    private  final VisitService visitService;


    public VideoFileService(
            @Autowired ResourceLoader resourceLoader,
            @Autowired VisitService visitService
    ) {
        this.resourceLoader = resourceLoader;
        this.visitService = visitService;
    }

    public Mono<Resource> getVideo(Video video) {
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

        try{
            File videoPath = new File("target/classes/videos/");
        }catch (Exception e){
            System.out.println("Error creating video folder");
        }

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
            ImageIO.write(bufferedImage, "png", new File("target/classes/thumbnails/" + title + ".png"));
            ImageIO.write(bufferedImage, "png", new File("src/main/resources/thumbnails/" + title + ".png"));
        }catch (IOException | JCodecException e){
            System.out.println("Error generating thumbnail");
        }
    }

    public Mono<Resource> getVideoThumbnail(String title){
        return Mono.fromSupplier(
                () -> resourceLoader.getResource("classpath:thumbnails/" + title + ".png")
        );
    }


    public void updateVideoTitle(String oldTitle, String newTitle) throws IOException {
        // Construir la ruta del archivo antiguo y del nuevo
        String oldFilePath = "target/classes/videos/" + oldTitle + VIDEO_EXTENSION;
        String newFilePath = "target/classes/videos/" + newTitle + VIDEO_EXTENSION;

        File oldFile = new File(oldFilePath);

        // Verificar si el archivo antiguo existe
        if (!oldFile.exists()) {
            throw new VideoNotFoundException("Video with title " + oldTitle + " not found.");
        }

        File newFile = new File(newFilePath);

        // Verificar si el archivo con el nuevo título ya existe
        if (newFile.exists()) {
            throw new IOException("A video with the new title " + newTitle + " already exists.");
        }

        // Realizar el cambio de nombre del archivo
        boolean renamed = oldFile.renameTo(newFile);

        if (!renamed) {
            throw new IOException("Failed to rename video from " + oldTitle + " to " + newTitle);
        }

        // Actualizar la miniatura si es necesario
        updateThumbnail(oldTitle, newTitle);
    }

    private void updateThumbnail(String oldTitle, String newTitle) {
        String oldThumbnailPath = "target/classes/thumbnails/" + oldTitle + THUMBNAIL_EXTENSION;
        String newThumbnailPath = "target/classes/thumbnails/" + newTitle + THUMBNAIL_EXTENSION;

        File oldThumbnail = new File(oldThumbnailPath);
        File newThumbnail = new File(newThumbnailPath);

        if (oldThumbnail.exists()) {
            boolean renamed = oldThumbnail.renameTo(newThumbnail);
            if (!renamed) {
                System.out.println("Failed to rename thumbnail for " + oldTitle);
            }
        }
    }


}