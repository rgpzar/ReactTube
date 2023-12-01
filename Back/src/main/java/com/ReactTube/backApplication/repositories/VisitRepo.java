package com.ReactTube.backApplication.repositories;

import com.ReactTube.backApplication.models.CompositeKeys.UserVideoPK;
import com.ReactTube.backApplication.models.Visit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface VisitRepo extends CrudRepository<Visit, UserVideoPK> {
    @Query(value = "SELECT * FROM visit v WHERE v.user_id = :userId AND v.video_id = :videoId", nativeQuery = true)
    Optional<Visit> findByUseridAndVideoId(long userId, long videoId);

    Set<Visit> findByIdVideoId(long videoId);
}
