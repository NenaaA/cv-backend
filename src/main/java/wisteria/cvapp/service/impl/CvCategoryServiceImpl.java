package wisteria.cvapp.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisteria.cvapp.model.Config;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.dto.CvDetailsDto;
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
            //get fields for the category
            List<Map<String, String>> categoryList = cvDetailsDto.getCategoryMap().get(categoryName);
            for (Map<String, String> fieldDtoMap : categoryList) {
                //create and save the cvCategory
                CvCategory cvCategory = categoryMapping(categoryName, fieldDtoMap, cv);
                if (cvCategory.getCategory() == null) {
                    log.error("CvCategory is null, exception while creating the cv category");
                    throw new RuntimeException("Cv category is null");
                }
                this.cvCategoryRepository.save(cvCategory);
                log.info("New cvCategory created with id={} and category={}, for cv with id={}",
                        cvCategory.getId(), cvCategory.getCategory(), cv.getId());
                //adding the category int the result list
                resultIdList.add(cvCategory.getId());
                // for every category save the category fields
                this.cvCategoryDetailsService.saveAllFieldsForCategory(cvCategory, fieldDtoMap);
            }
        }
        return resultIdList;
    }

    @Transactional
    @Override
    public void deleteAll(@NotNull Cv cv) {
        //find all categories by the cv
        List<CvCategory> cvCategoryList = this.cvCategoryRepository.findAllByCv(cv.getId());
        try {
            this.cvCategoryDetailsService.deleteAllFieldsForCategoryList(cvCategoryList);
            this.cvCategoryRepository.deleteAllByCv(cv);
        } catch (Exception e) {
            log.error("Exception during deleting the cv category, exception=" + e);
            throw new RuntimeException("Deleting category not successful");
        }
    }

    @Override
    public List<Integer> getCategoryIdsByCvId(Integer cvId) {
        return this.cvCategoryRepository.findCategoryIdsByCvId(cvId);
    }


    @Override
    public Map<String, List<Map<String, String>>> getFieldDetailsForCvId(Integer cvId) {
        List<Integer> categoryIdsForCv = getCategoryIdsByCvId(cvId);
        return this.cvCategoryDetailsService.getCvCategoryDetailsForCategoryIdList(categoryIdsForCv);
    }

    private CvCategory categoryMapping(String categoryName,
                                       Map<String, String> fieldDtoMap,
                                       Cv cv) {
        //get the categories present in the DB
        List<Config> categoryNameAndRuleList = this.configService
                .getConfigByGroupAndAttribute("Category", "CategoryName");
        //create the cvCategory name
        String cvCategoryName = cvCategoryNameMapping(categoryName.toUpperCase(),
                categoryNameAndRuleList,
                fieldDtoMap);
        //create the cvCategory
        CvCategory cvCategory = new CvCategory();
        cvCategory.setCategory(categoryName);
        cvCategory.setName(cvCategoryName);
        cvCategory.setCv(cv);
        return cvCategory;
    }

    private String cvCategoryNameMapping(String categoryName,
                                         List<Config> categoryNameAndRuleList,
                                         Map<String, String> fieldDtoMap) {
        //get the label list for the category name
        String categoryNameLabelRule = categoryNameAndRuleList.stream()
                .filter(config -> categoryName.equalsIgnoreCase(config.getValue()))
                .map(Config::getAdditionalInfo)
                .findFirst()
                .orElse(null);
        //if there is no rules for the category
        if (categoryNameLabelRule == null) {
            log.error("There is no rule for creating the name of the category {}, please enter the rule in the DB", categoryName);
            throw new RuntimeException("Error while creating the cv");
        }
        List<String> labelList = Arrays.asList(categoryNameLabelRule.split(","));
        //get the label values for the label in the list and create the category name
        return fieldDtoMap.values().stream()
                .filter(cvDetailsFieldDto -> labelList.contains(cvDetailsFieldDto.toUpperCase()))
                .collect(Collectors.joining("_"));
    }
}
