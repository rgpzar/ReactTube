package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VisitNotFoundException;
import com.ReactTube.backApplication.models.CompositeKeys.UserVideoPK;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.repositories.UserRepo;
import com.ReactTube.backApplication.repositories.VideoRepo;
import com.ReactTube.backApplication.repositories.VisitRepo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@Builder
@Data
@EqualsAndHashCode
@lombok.Value
public class VisitService {

    int MAX_MINUTES_BETWEEN_VISITS;

    VisitRepo visitRepo;
    AuthenticationService authenticationService;
    UserService userService;

    public VisitService(
            @Value("${stream_conf.visit.max_minutes_between_visits}") int MAX_MINUTES_BETWEEN_VISITS,
            VisitRepo visitRepo,
            AuthenticationService authenticationService,
            UserService userService)
        {
        this.MAX_MINUTES_BETWEEN_VISITS = MAX_MINUTES_BETWEEN_VISITS;
        this.visitRepo = visitRepo;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    public Set<Visit> getVisitsByVideoId(long videoId){
    return visitRepo.findByIdVideoId(videoId);
}

    public void addVisit(Video video) throws NoUserAuthorizedException {
        User visitor = null;

        try{
            visitor = authenticationService.getCurrentAuthenticatedUser();

            System.out.printf("Username: %s visited the video with id: %d %n", visitor .getUsername(), video.getId());
        }catch (NoUserAuthorizedException e){
            visitor = userService.getAnonymousUser();

            System.out.println("Anonymous visit to the video with id: " + video.getId());
        }finally {
            assert visitor != null;
            Visit lastVisit = visitRepo.findByUseridAndVideoId(visitor.getId(), video.getId()).orElse(null);

            if (lastVisit != null){
                Date lastVisitTime = lastVisit.getId().getTime();
                Date now = new Date();

                long differenceInMs = Math.abs(lastVisitTime.getTime() - now.getTime());
                long differenceInMinutes = differenceInMs / (60 * 1000);

                if(differenceInMinutes > MAX_MINUTES_BETWEEN_VISITS){
                    System.out.println("Visit added");
                    manageVisit(visitor.getId(), video.getId());
                }
            }else{
                manageVisit(visitor.getId(), video.getId());
            }
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
            Visit newVisit = visit;
            visitRepo.delete(visit);

           UserVideoPK newVisitPK =  new UserVideoPK();

           newVisitPK.setUserId(newVisit.getId().getUserId());
           newVisitPK.setVideoId(newVisit.getId().getVideoId());
           newVisitPK.setTime(new Date());

            int timesVisited = visit.getTimesVisited();
            newVisit.setTimesVisited(timesVisited + 1);
            newVisit.setId(newVisitPK);


            visitRepo.save(newVisit);
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

    public Visit updateLike(long videoId, Boolean like) throws NoUserAuthorizedException {
        User user = authenticationService.getCurrentAuthenticatedUser();

        Optional<Visit> visitOptional = visitRepo.findByUseridAndVideoId(user.getId(), videoId);
        Visit visit = visitOptional.orElseThrow(() -> new VisitNotFoundException("Visit couldn't be found"));
        visit.setUserLike(like);

        //persist
        return visitRepo.save(visit);//else throw new VisitNotFoundException()
    }
}
