package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.CvCategoryDetails;


import java.util.List;
import java.util.Map;

public interface CvCategoryDetailsService {
    List<CvCategoryDetails> saveAllFieldsForCategory(@NotNull CvCategory cvCategory,
                                                     @NonNull Map<String,String> fieldDtoMap);

    List<Integer> deleteAllFieldsForCategoryList(@NotNull List<CvCategory> cvCategoryList);
    Map<String,List<Map<String,String>>> getCvCategoryDetailsForCategoryIdList(@NotNull List<Integer> categoryIds);

}
