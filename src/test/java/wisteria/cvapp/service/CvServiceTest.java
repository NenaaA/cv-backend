package wisteria.cvapp.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import wisteria.cvapp.TestHelpers;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;
import wisteria.cvapp.model.dto.CvDetailsFieldDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest()
@Slf4j
class CvServiceTest {

    @Autowired
    private CvService cvService;
    @Test
    void getAllCvForUserId() {
        Integer userId=1;
        List<Cv> listCv=this.cvService.getAllCvForUserId(userId);
        listCv.forEach(cv->log.info("User cv id={},user={},name={}",cv.getId(),cv.getUser(),cv.getName()));
    }

    @Test
    void getCv() {
        Integer cvId=5;
        Integer userId=1;
        //this.cvService.getCv(cvId,userId);
        log.info(TestHelpers.objectToJson(this.cvService.getCv(cvId,userId)));
    }

    @Test
    void createCv() {
        Integer userId=1;
        CvDetailsDto cvDetailsDto=new CvDetailsDto();
        cvDetailsDto.setUserId(userId);
        List<CvDetailsFieldDto> personalInfo=Arrays.asList(new CvDetailsFieldDto("PersonalInformation","Name","Jana"),
                new CvDetailsFieldDto("PersonalInformation","Surname","Gelevska"),
                new CvDetailsFieldDto("PersonalInformation","Email","jana.gelevska@gmail.com"),
                new CvDetailsFieldDto("PersonalInformation","PhoneNumber","+38972301105"));
        cvDetailsDto.getCategoryMap().put("PersonalInformation",Arrays.asList(personalInfo));

        List<CvDetailsFieldDto> educationInfo=Arrays.asList(new CvDetailsFieldDto("Education","School","RJK"),
                new CvDetailsFieldDto("Education","Degree","GRAD"),
                new CvDetailsFieldDto("Education","StartDate","1.9.2017"),
                new CvDetailsFieldDto("Education","EndDate","20.6.2022"));
        List<CvDetailsFieldDto> educationIngoTmp=Arrays.asList(new CvDetailsFieldDto("Education","School","FINKI"),
                new CvDetailsFieldDto("Education","Degree","BACH"),
                new CvDetailsFieldDto("Education","StartDate","1.9.2022"),
                new CvDetailsFieldDto("Education","EndDate","20.6.2024"));
        cvDetailsDto.getCategoryMap().put("Education",Arrays.asList(educationInfo,educationIngoTmp));
       log.info("Test successful with result {}",this.cvService.createCv(cvDetailsDto));
    }

    @Test
    void updateCv() {
    }

    @Test
    void deleteCv() {
        Integer cvId=5;
        log.info("Test successful with result {}",cvService.deleteCv(cvId));
    }
}