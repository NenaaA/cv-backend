package wisteria.cvapp.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.Config;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.dto.CvDetailsDto;
import wisteria.cvapp.model.dto.CvDetailsFieldDto;
import wisteria.cvapp.repository.CvCategoryRepository;
import wisteria.cvapp.service.ConfigService;
import wisteria.cvapp.service.CvCategoryDetailsService;
import wisteria.cvapp.service.CvCategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CvCategoryServiceImpl implements CvCategoryService {
    private final CvCategoryRepository cvCategoryRepository;
    private final CvCategoryDetailsService cvCategoryDetailsService;
    private final ConfigService configService;

    @Override
    public List<Integer> saveAll(@NonNull CvDetailsDto cvDetailsDto, @NotNull Cv cv) {
        List<Integer> resultIdList = new ArrayList<>();
        //create cv category for every key
        for (String categoryName : cvDetailsDto.getCategoryMap().keySet()) {
            List<List<CvDetailsFieldDto>> categoryList = cvDetailsDto.getCategoryMap().get(categoryName);
            for (List<CvDetailsFieldDto> fieldDtoList : categoryList) {
                //get fields for the category
                //create and save the cvCategory
                CvCategory cvCategory = categoryMapping(categoryName, fieldDtoList, cv);
                if (cvCategory == null) {
                    log.error("CvCategory is null, exception while creating the cv category");
                    throw new RuntimeException("Cv category is null");
                }
                this.cvCategoryRepository.save(cvCategory);
                log.info("New cvCategory created with id={} and category={}, for cv with id={}",
                        cvCategory.getId(), cvCategory.getCategory(), cv.getId());
                resultIdList.add(cvCategory.getId());
                // for every category save the category fields
                this.cvCategoryDetailsService.saveAllFieldsForCategory(cvCategory, fieldDtoList);
            }
        }
        return resultIdList;
    }

    @Override
    public List<Integer> deleteAll(@NotNull Cv cv) {
        List<CvCategory> cvCategoryList = this.cvCategoryRepository.findAllByCv(cv.getId());
        try {
            this.cvCategoryDetailsService.deleteAllFieldsForCategoryList(cvCategoryList);
            this.cvCategoryRepository.deleteAllByCv(cv);
        } catch (Exception e) {
            log.error("Exception during deleting the cv category, exception=" + e);
            throw new RuntimeException("Deleting category not successful");
        }
        return null;
    }

    @Override
    public List<Integer> getCategoryIdsByCvIdAndUserId(Integer cvId, Integer userId) {
        return this.cvCategoryRepository.findCategoryIdsByCvId(cvId, userId);
    }


    @Override
    public Map<String, List<List<CvDetailsFieldDto>>> getFieldDetailsForCvId(Integer cvId, Integer userId) {
        List<Integer> categoryIdsForCv = getCategoryIdsByCvIdAndUserId(cvId, userId);
        return this.cvCategoryDetailsService.getCvCategoryDetailsForCategoryIdList(categoryIdsForCv);
    }

    private CvCategory categoryMapping(String categoryName,
                                       List<CvDetailsFieldDto> fieldDtoList,
                                       Cv cv) {

        //get the categories present in the DB
        List<Config> categoryNameAndRuleList = this.configService
                .getConfigByGroupAndAttribute("Category", "CategoryName");
        //TODO:if there is a new category write it in the db
        //create the cvCategory name
        String cvCategoryName = cvCategoryNameMapping(categoryName.toUpperCase(),
                categoryNameAndRuleList,
                fieldDtoList);
        if (cvCategoryName == null)
            return null;
        //create the cvCategory
        CvCategory cvCategory = new CvCategory();
        cvCategory.setCategory(categoryName);
        cvCategory.setName(cvCategoryName);
        cvCategory.setCv(cv);
        return cvCategory;
    }

    private String cvCategoryNameMapping(String categoryName,
                                         List<Config> categoryNameAndRuleList,
                                         List<CvDetailsFieldDto> fieldDtoList) {
        //get the label list for the category name
        String categoryNameLabelRule = categoryNameAndRuleList.stream()
                .filter(config -> categoryName.equalsIgnoreCase(config.getValue()))
                .map(Config::getAdditionalInfo)
                .findFirst()
                .orElse(null);
        if (categoryNameLabelRule == null)
            return null;
        List<String> labelList = Arrays.asList(categoryNameLabelRule.split(","));
        //get the label values for the label in the list and create the category name
        return fieldDtoList.stream()
                .filter(cvDetailsFieldDto -> labelList.contains(cvDetailsFieldDto.getLabel().toUpperCase()))
                .map(CvDetailsFieldDto::getValue)
                .collect(Collectors.joining("_"));
    }
}
