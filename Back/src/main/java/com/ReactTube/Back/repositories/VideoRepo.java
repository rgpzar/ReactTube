package com.ReactTube.Back.repositories;

import com.ReactTube.Back.models.Video;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepo extends CrudRepository<Video, Long> {
}
