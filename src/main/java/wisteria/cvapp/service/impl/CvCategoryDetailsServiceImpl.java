package wisteria.cvapp.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.CvCategoryDetails;
import wisteria.cvapp.model.projection.CvCategoryFieldProjection;
import wisteria.cvapp.repository.CvCategoryDetailsRepository;
import wisteria.cvapp.service.CvCategoryDetailsService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CvCategoryDetailsServiceImpl implements CvCategoryDetailsService {
    private final CvCategoryDetailsRepository cvCategoryDetailsRepository;

    @Override
    public List<CvCategoryDetails> saveAllFieldsForCategory(@NotNull CvCategory cvCategory,
                                                            @NonNull Map<String, String> fieldDtoMap) {
        //mapping each field to cvCategoryDetails model for inserting in DB
        List<CvCategoryDetails> cvCategoryDetailsList = fieldDtoMap.entrySet()
                .stream()
                .map(entry -> {
                    CvCategoryDetails cvCategoryDetails = new CvCategoryDetails();
                    cvCategoryDetails.setCvCategory(cvCategory);
                    cvCategoryDetails.setLabel(entry.getKey());
                    cvCategoryDetails.setValue(entry.getValue());
                    return cvCategoryDetails;
                }).collect(Collectors.toList());
        //saving all fields for the category in DB
        this.cvCategoryDetailsRepository.saveAll(cvCategoryDetailsList);
        log.info("All fields for cvCategory with category={} were successfully created in cvCategoryDetails",
                cvCategory.getCategory());
        return cvCategoryDetailsList;
    }

    @Override
    public List<Integer> deleteAllFieldsForCategoryList(@NotNull List<CvCategory> cvCategoryList) {
        this.cvCategoryDetailsRepository.deleteAllByCvCategoryIsIn(cvCategoryList);
        return null;
    }


    @Override
    public Map<String, List<Map<String, String>>> getCvCategoryDetailsForCategoryIdList(List<Integer> categoryIds) {
        //get cvDetails for every field from DB
        List<CvCategoryFieldProjection> cvDetailsFields = this.cvCategoryDetailsRepository.getCvDetails(categoryIds);
        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        Map<Integer, List<CvCategoryFieldProjection>> projectionMap = cvDetailsFields.stream()
                .collect(Collectors.groupingBy(CvCategoryFieldProjection::getCategoryId));
        //map the values, if the key is not present new array is created otherwise the map of fields is added
        for (Map.Entry<Integer, List<CvCategoryFieldProjection>> entry : projectionMap.entrySet()) {
            String categoryKey = (entry.getValue().get(0).getCategoryName());
            resultMap.computeIfAbsent(categoryKey, k -> new ArrayList<>()).add(fieldMappingFromProjection(entry.getValue()));
        }
        return resultMap;
    }

    private Map<String, String> fieldMappingFromProjection(List<CvCategoryFieldProjection> fieldProjections) {
        return fieldProjections.stream()
                .collect(Collectors.toMap(CvCategoryFieldProjection::getLabel, CvCategoryFieldProjection::getValue));

    }
}
