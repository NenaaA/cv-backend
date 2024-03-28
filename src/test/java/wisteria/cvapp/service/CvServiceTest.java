package wisteria.cvapp.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import wisteria.cvapp.TestHelpers;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest()
@Slf4j
class CvServiceTest {

    @Autowired
    private CvService cvService;

    @Test
    void getAllCvForUserId() {
        Integer userId = 1;
        List<Cv> listCv = this.cvService.getAllCvForUserId(userId);
        listCv.forEach(cv -> log.info("User cv id={},user={},name={}", cv.getId(), cv.getUser(), cv.getName()));
    }

    @Test
    void getCv() {
        Integer cvId = 5;
        Integer userId = 1;
        //this.cvService.getCv(cvId,userId);
        log.info(TestHelpers.objectToJson(this.cvService.getCv(cvId)));
    }

    @Test
    void createCv() {
        Integer userId = 2;
        CvDetailsDto cvDetailsDto = new CvDetailsDto();
        cvDetailsDto.setUserId(userId);
        Map<String, String> fieldMapPersonalInfo = new HashMap<>();
        fieldMapPersonalInfo.put("Name", "Nena");
        fieldMapPersonalInfo.put("Surname", "Anastasova");
        fieldMapPersonalInfo.put("Email", "neananena@gmail.com");
        fieldMapPersonalInfo.put("PhoneNumber", "+38972255633");
        cvDetailsDto.getCategoryMap().put("PersonalInformation", Arrays.asList(fieldMapPersonalInfo));

        Map<String, String> fieldMapEducationOne = new HashMap<>();
        fieldMapEducationOne.put("School", "ORCE");
        fieldMapEducationOne.put("Degree", "GRAD");
        fieldMapEducationOne.put("StartDate", "1.9.2017");
        fieldMapEducationOne.put("EndDate", "20.6.2022");

        Map<String, String> fieldMapEducationTwo = new HashMap<>();
        fieldMapEducationTwo.put("School", "FINKI");
        fieldMapEducationTwo.put("Degree", "BACH");
        fieldMapEducationTwo.put("StartDate", "1.9.2022");
        fieldMapEducationTwo.put("EndDate", "20.6.2024");
        cvDetailsDto.getCategoryMap().put("Education", Arrays.asList(fieldMapEducationOne, fieldMapEducationTwo));
        log.info("Test successful with result {}", this.cvService.createCv(cvDetailsDto));
    }

    @Test
    void updateCv() {
        Integer userId = 1;
        Integer cvId = 5;
        CvDetailsDto cvDetailsDto = new CvDetailsDto();
        cvDetailsDto.setUserId(userId);

        Map<String, String> skillInfo = new HashMap<>();
        skillInfo.put("Skill", "Talking");
        skillInfo.put("Degree", "per");
        Map<String, String> skillINfoTmp = new HashMap<>();
        skillINfoTmp.put("Skill", "Spring");
        skillINfoTmp.put("Degree", "per");
        cvDetailsDto.getCategoryMap().put("Skills", Arrays.asList(skillInfo, skillINfoTmp));
        log.info("Test successful with result {}", this.cvService.updateCv(cvId, cvDetailsDto));
    }

    @Test
    void deleteCv() {
        Integer cvId = 5;
        log.info("Test successful with result {}", cvService.deleteCv(cvId));
    }
}