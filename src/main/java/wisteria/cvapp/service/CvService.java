package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.dto.CvDetailsDto;


import java.util.List;


public interface CvService {
     List<Cv> getAllCvForUserId(@NotNull Integer userId);
     CvDetailsDto getCv(@NotNull Integer cvId, @NonNull Integer userId);
     Integer createCv( @NotNull CvDetailsDto cvDetailsDto);
     Integer updateCv( @NotNull Integer cvId,@NotNull CvDetailsDto cvDetailsDto);
     Integer deleteCv( @NotNull Integer cvId);
}
