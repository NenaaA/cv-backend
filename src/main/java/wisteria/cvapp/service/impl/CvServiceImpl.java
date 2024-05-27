package wisteria.cvapp.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.User;
import wisteria.cvapp.model.dto.CvDetailsDto;
import wisteria.cvapp.repository.CvRepository;
import wisteria.cvapp.service.CvCategoryService;
import wisteria.cvapp.service.CvService;
import wisteria.cvapp.service.UserService;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class CvServiceImpl implements CvService {
    private final CvRepository cvRepository;
    private final CvCategoryService cvCategoryService;
    private final UserService userService;


    @Override
    public List<Cv> getAllCvForUserId(@NonNull Integer userId) {
        if(!userService.checkJwtAuthentication(userId))
        {
            log.error("Logged in user does not match user id={} while retrieving cv list",userId);
            throw new RuntimeException("The logged in user does not match the user id provided");
        }
        return cvRepository.findAllByUser_Id(userId);
    }

    @Override
    public CvDetailsDto getCv(@NotNull Integer cvId) {
        checkIfTheLoggedInUserCorresponds(cvId);
        //get fields for the cv
        Map<String, List<Map<String,String>>> cvDetailsMap = this.cvCategoryService.getFieldDetailsForCvId(cvId);
        //create the response
        CvDetailsDto cvDetailsDto = new CvDetailsDto();
        cvDetailsDto.setUserId(this.userService.getLoggedInUser().getId());
        cvDetailsDto.getCategoryMap().putAll(cvDetailsMap);
        return cvDetailsDto;

    }

    public static Timestamp convertToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy / HH:mm");
        return localDateTime.format(formatter);
    }

    @Override
    public Integer createCv(@NonNull CvDetailsDto cvDetailsDto) {
        //get the user
        User user = this.userService.getUserById(cvDetailsDto.getUserId());

        //check if user is null
        if (user == null) {
            log.error("User is null for user id={}, while creating a new cv", cvDetailsDto.getUserId());
            throw new RuntimeException("The user is not valid");
        }
        if(!userService.checkJwtAuthentication(user.getId()))
        {
            log.error("Logged in user does not match user id={} while retrieving cv list",user.getId());
            throw new RuntimeException("The logged in user does not match the user id provided");
        }
        //create cv
        Cv cv = new Cv();
        cv.setName(cvDetailsDto.getCategoryMap().get("PersonalInformation").get(0).get("JobTitle") + " " + formatLocalDateTime(LocalDateTime.now()));
        cv.setUser(user);
        cv.setCreated_at(convertToTimestamp(LocalDateTime.now()));
        Cv newCv = this.cvRepository.save(cv);
        log.info("New cv created with id={}, for user userId={} and username={}",
                newCv.getId(), user.getId(), user.getUsername());
        //create cv_category
        this.cvCategoryService.saveAll(cvDetailsDto, newCv);
        return cv.getId();
    }

    @Override
    public Integer updateCv(@NotNull Integer cvId, @NonNull CvDetailsDto cvDetailsDto) {
        //get the user
        User user = this.userService.getUserById(cvDetailsDto.getUserId());
        //check if user is null
        if (user == null) {
            log.error("User is null for user id={}, while updating a cv with id={}", cvDetailsDto.getUserId(), cvId);
            throw new RuntimeException("The user is not valid");
        }
        if(!userService.checkJwtAuthentication(user.getId()))
        {
            log.error("Logged in user does not match user id={} while retrieving cv list",user.getId());
            throw new RuntimeException("The logged in user does not match the user id provided");
        }
        //find the cv
        Cv cv = this.cvRepository.getCvById(cvId);
        if (cv == null) {
            log.error("Cv is null while updating cv with id={}", cvId);
            throw new RuntimeException("The cv is not valid ");
        }
        //delete all categories associated to the cv
        this.cvCategoryService.deleteAll(cv);
        //add the new categories
        this.cvCategoryService.saveAll(cvDetailsDto, cv);
        return cvId;
    }

    @Override
    @Transactional
    public Integer deleteCv(@NotNull Integer cvId) {
        checkIfTheLoggedInUserCorresponds(cvId);
        //find the cv
        Cv cv = this.cvRepository.getCvById(cvId);
        if (cv == null) {
            log.error("Cv is null while deleting the cv with id={}", cvId);
            throw new RuntimeException("The cv is not valid ");
        }
        //delete all categories and associated
        this.cvCategoryService.deleteAll(cv);
        //delete the cv
        this.cvRepository.delete(cv);

        return cvId;
    }

    private void checkIfTheLoggedInUserCorresponds(Integer cvId){
        //Check if the logged in user has the cv
        User loggedInUser=this.userService.getLoggedInUser();
        List<Cv> cvListForUser=cvRepository.findAllByUser_Id(loggedInUser.getId());
        if(cvListForUser.stream().noneMatch(cv->cv.getId().equals(cvId)))
        {
            log.error("The user with id {} is attempting to get the cv with id {} which is not in his cv list",loggedInUser.getId(),cvId);
            throw new RuntimeException("The cv id is not present in the logged in user list");
        }
    }

}
