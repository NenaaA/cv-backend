package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;

import java.util.List;
import java.util.Map;

public interface CvCategoryService {
    List<Integer> saveAll(@NonNull CvDetailsDto cvDetailsDto, @NotNull Cv cv);
    void deleteAll(@NotNull Cv cv);
    List<Integer> getCategoryIdsByCvId(@NotNull Integer cvId);
    Map<String, List<Map<String,String>>> getFieldDetailsForCvId(@NotNull Integer cvId);
}
