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
import wisteria.cvapp.model.dto.CvDetailsFieldDto;
import wisteria.cvapp.repository.CvRepository;
import wisteria.cvapp.service.CvCategoryService;
import wisteria.cvapp.service.CvService;
import wisteria.cvapp.service.UserService;


import java.time.LocalDateTime;
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
        // List<List<String>> cvDetailsFieldDbValues = this.cvRepository.getCvDetails(cvId, userId);
//       cvDetailsDtos.forEach(dto->cvDetailsMap.put(dto.getCategory(), new ArrayList<>()));
//        cvDetailsDtos.forEach(dto->cvDetailsMap.get(dto.getCategory()).add(dto));
        //find all cv category ids for the cv
//        List<Integer> categoryIds=this.cvCategoryService.getCategoryIdsByCvIdAndUserId(cvId,userId);
//        List<CvDetailsFieldDto> cvDetailsFieldDtos = cvDetailsFieldDbValues.stream().map(fieldList -> {
//                    if (fieldList.size() == 3)
//                        return new CvDetailsFieldDto(fieldList.get(0), fieldList.get(1), fieldList.get(2));
//                    return null;
//                }
//        ).collect(Collectors.toList());
//        Map<String, List<CvDetailsFieldDto>> cvDetailsMap = cvDetailsFieldDtos.stream()
//                .collect(Collectors.groupingBy(CvDetailsFieldDto::getCategory));
        Map<String, List<List<CvDetailsFieldDto>>> cvDetailsMap = this.cvCategoryService.getFieldDetailsForCvId(cvId);
        CvDetailsDto cvDetailsDto = new CvDetailsDto();
        cvDetailsDto.setUserId(this.userService.getLoggedInUser().getId());
        cvDetailsDto.getCategoryMap().putAll(cvDetailsMap);
        return cvDetailsDto;

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
        // nomenclature cv_username_date
        cv.setName("cv_" + user.getUsername() + "_" + LocalDateTime.now());
        cv.setUser(user);
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
        this.cvCategoryService.saveAll(cvDetailsDto, cv);
        return cvId;
    }

    @Override
    @Transactional
    public Integer deleteCv(@NotNull Integer cvId) {
        //find the cv
        Cv cv = this.cvRepository.getCvById(cvId);
        if (cv == null) {
            log.error("Cv is null while deleting the cv with id={}", cvId);
            throw new RuntimeException("The cv is not valid ");
        }
        this.cvCategoryService.deleteAll(cv);
        this.cvRepository.delete(cv);

        return cvId;
    }

}
