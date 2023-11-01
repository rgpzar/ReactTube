package com.ReactTube.Back.services;

import com.ReactTube.Back.models.CompositeKeys.UserVideoPK;
import com.ReactTube.Back.models.User;
import com.ReactTube.Back.models.Video;
import com.ReactTube.Back.models.Visit;
import com.ReactTube.Back.repositories.UserRepo;
import com.ReactTube.Back.repositories.VideoRepo;
import com.ReactTube.Back.repositories.VisitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class VisitService {
    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VideoRepo videoRepo;

    public Set<Visit> getVisitsByVideoId(long videoId){
        return visitRepo.findByIdVideoId(videoId);
    }

    public void addVisit(Video video){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            User user = userRepo.findByUsername(auth.getName()).orElse(null); //throw UserNotFoundException -> Self-made

            assert user != null;
            assert video != null;
            manageVisit(user.getId(), video.getId());
        }
    }

    private void manageVisit(long userId, long videoId){
        Optional<Visit> visitOptional = visitRepo.findByUseridAndVideoId(userId, videoId);

        //Check if there is a visit record for this user and video (the user already visited this video)
        if(visitOptional.isPresent()){
            /*
             * Update times visited for the record
             *
             * */
            Visit visit = visitOptional.get();
            int timesVisited = visit.getTimesVisited();
            visit.setTimesVisited(timesVisited + 1);

            visitRepo.save(visit);
        }else{
            /*
             * Insert the first visit for this user and video id's
             *
             * */
            Visit firstVisit = new Visit();
            UserVideoPK visitId = new UserVideoPK();

            visitId.setVideoId(videoId);
            visitId.setUserId(userId);
            visitId.setTime(new Date());

            firstVisit.setId(visitId);
            firstVisit.setTimesVisited(1);
            firstVisit.setUserLike(false);

            visitRepo.save(firstVisit);
        }
    }

    public Visit updateLike(long videoId, Boolean like){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(auth.getName()).orElse(null);
        /*TO DO: Replace the previous 2 lines of code by a new method to get the current authenticated user (AuthenticationService)*/

        Optional<Visit> visitOptional = visitRepo.findByUseridAndVideoId(user.getId(), videoId);

        if(visitOptional.isPresent()){
            Visit visit = visitOptional.get();
            visit.setUserLike(like);

            //persist
            return visitRepo.save(visit);
        }//else throw new VisitNotFoundException()

        return null;
    }
}
