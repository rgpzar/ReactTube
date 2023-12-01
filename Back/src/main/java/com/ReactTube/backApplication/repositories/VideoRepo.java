package com.ReactTube.backApplication.repositories;

import com.ReactTube.backApplication.models.Video;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepo extends CrudRepository<Video, Long> {
}
