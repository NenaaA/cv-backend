package wisteria.cvapp.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.CvCategoryDetails;
import wisteria.cvapp.model.dto.CvDetailsFieldDto;
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
                                                            @NonNull List<CvDetailsFieldDto> fieldDtoList) {
        List<CvCategoryDetails> cvCategoryDetailsList = fieldDtoList
                .stream()
                .map(dto -> {
                    CvCategoryDetails cvCategoryDetails = new CvCategoryDetails();
                    cvCategoryDetails.setCvCategory(cvCategory);
                    cvCategoryDetails.setLabel(dto.getLabel());
                    cvCategoryDetails.setValue(dto.getValue());
                    return cvCategoryDetails;
                }).collect(Collectors.toList());
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
    public Map<String, List<List<CvDetailsFieldDto>>> getCvCategoryDetailsForCategoryIdList(List<Integer> categoryIds) {
        List<CvCategoryFieldProjection> cvDetailsFields = this.cvCategoryDetailsRepository.getCvDetails(categoryIds);
        Map<String, List<List<CvDetailsFieldDto>>> resultMap = new HashMap<>();
        Map<Integer, List<CvCategoryFieldProjection>> projectionMap = cvDetailsFields.stream()
                .collect(Collectors.groupingBy(CvCategoryFieldProjection::getCategoryId));
        for (Map.Entry<Integer, List<CvCategoryFieldProjection>> entry : projectionMap.entrySet()) {
            String categoryKey = (entry.getValue().get(0).getCategoryName());
            resultMap.computeIfAbsent(categoryKey, k -> new ArrayList<>()).add(fieldMappingFromProjection(entry.getValue()));
        }
        return resultMap;
    }
    private List<CvDetailsFieldDto> fieldMappingFromProjection(List<CvCategoryFieldProjection> fieldProjections) {
        return fieldProjections.stream()
                .map(projection -> new CvDetailsFieldDto(projection.getCategoryName(),
                        projection.getLabel(),
                        projection.getValue()))
                .collect(Collectors.toList());
    }
}
