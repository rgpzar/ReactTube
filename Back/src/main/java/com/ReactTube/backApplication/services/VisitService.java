package com.ReactTube.backApplication.services;

import com.ReactTube.backApplication.errorHandling.customExceptions.NoUserAuthorizedException;
import com.ReactTube.backApplication.errorHandling.customExceptions.VisitNotFoundException;
import com.ReactTube.backApplication.models.CompositeKeys.UserVideoPK;
import com.ReactTube.backApplication.models.User;
import com.ReactTube.backApplication.models.Video;
import com.ReactTube.backApplication.models.Visit;
import com.ReactTube.backApplication.repositories.VisitRepo;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@Data
public class VisitService {

    int MAX_MINUTES_BETWEEN_VISITS;

    private VisitRepo visitRepo;
    private AuthenticationService authenticationService;
    private UserService userService;

    public VisitService(
            @Value("${stream_conf.visit.max_minutes_between_visits}") int MAX_MINUTES_BETWEEN_VISITS,
            @Autowired VisitRepo visitRepo,
            @Autowired AuthenticationService authenticationService,
            @Autowired UserService userService)
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
        }catch (AuthenticationCredentialsNotFoundException e){
            visitor = userService.getAnonymousUser();

            System.out.println("Anonymous visit to the video with id: " + video.getId());
        }finally {
            assert visitor != null;
            Visit lastVisit = visitRepo.findByUseridAndVideoId(visitor.getId(), video.getId()).orElse(null);

            System.out.println(
                    lastVisit == null ?
                            "No previous visit found" :
                            "Last visit found: " + lastVisit
            );

            if (lastVisit != null){
                Date lastVisitTime = lastVisit.getId().getTime();
                Date now = new Date();

                long differenceInMs = Math.abs(now.getTime() - lastVisitTime.getTime());
                long differenceInMinutes = differenceInMs / (60 * 1000);

                if(differenceInMinutes > MAX_MINUTES_BETWEEN_VISITS){
                    System.out.println("Visit added");

                    Visit newVisit = new Visit();
                    UserVideoPK visitId = new UserVideoPK();

                    visitId.setVideoId(video.getId());
                    visitId.setUserId(visitor.getId());
                    visitId.setTime(new Date());

                    newVisit.setId(visitId);

                    visitRepo.save(newVisit);
                }
            }else{
                Visit newVisit = new Visit();
                UserVideoPK visitId = new UserVideoPK();

                visitId.setVideoId(video.getId());
                visitId.setUserId(visitor.getId());
                visitId.setTime(new Date());

                newVisit.setId(visitId);

                visitRepo.save(newVisit);
            }
        }

    }



    public Visit updateLike(long videoId, Boolean like) throws NoUserAuthorizedException {
        User user = authenticationService.getCurrentAuthenticatedUser();

        Optional<Visit> visitOptional = visitRepo.findByUseridAndVideoId(user.getId(), videoId);
        Visit visit = visitOptional.orElseThrow(() -> new VisitNotFoundException("Visit couldn't be found"));

        //persist
        return visitRepo.save(visit);//else throw new VisitNotFoundException()
    }
}
