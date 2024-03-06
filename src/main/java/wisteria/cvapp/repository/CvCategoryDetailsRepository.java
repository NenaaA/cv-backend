package wisteria.cvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import wisteria.cvapp.common.QueryConstants;
import wisteria.cvapp.model.CvCategory;
import wisteria.cvapp.model.CvCategoryDetails;

import java.util.List;

public interface CvCategoryDetailsRepository extends JpaRepository<CvCategoryDetails, Integer> {
    void deleteAllByCvCategoryIsIn(List<CvCategory> cvCategoryList);
    @Query(value = QueryConstants.getCvDetails, nativeQuery = true)
    List<CvCategoryDetails> getCvDetails(List<Integer> categoryIds);

}
