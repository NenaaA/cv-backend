package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.CvCategoryDetails;
import wisteria.cvapp.model.dto.CvDetailsFieldDto;

import java.util.List;
import java.util.Map;

public interface CvCategoryDetailsService {
    List<CvCategoryDetails> saveAllFieldsForCategory(@NotNull CvCategory cvCategory,
                                                     @NonNull List<CvDetailsFieldDto> fieldDtoList);

    List<Integer> deleteAllFieldsForCategoryList(@NotNull List<CvCategory> cvCategoryList);
    Map<String,List<List<CvDetailsFieldDto>>> getCvCategoryDetailsForCategoryIdList(@NotNull List<Integer> categoryIds);

}
