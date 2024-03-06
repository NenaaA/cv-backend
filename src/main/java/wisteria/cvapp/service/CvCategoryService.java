package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;
import wisteria.cvapp.model.dto.CvDetailsFieldDto;

import java.util.List;
import java.util.Map;

public interface CvCategoryService {
    List<Integer> saveAll(@NonNull CvDetailsDto cvDetailsDto, @NotNull Cv cv);
    List<Integer> deleteAll(@NotNull Cv cv);
    List<Integer> getCategoryIdsByCvIdAndUserId(@NotNull Integer cvId, @NotNull Integer userId);
    Map<String,List<List<CvDetailsFieldDto>>> getFieldDetailsForCategoryIdList(@NotNull List<Integer> categoryIds);
}
