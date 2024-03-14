package wisteria.cvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wisteria.cvapp.common.QueryConstants;
import wisteria.cvapp.model.Cv;
import wisteria.cvapp.model.CvCategory;

import java.util.List;


public interface CvCategoryRepository extends JpaRepository<CvCategory, Integer> {
    void deleteAllByCv(Cv cv);
    @Query(value = QueryConstants.getCvCategoriesByCvId, nativeQuery = true)
    List<CvCategory> findAllByCv(Integer cvId);

    @Query(value = QueryConstants.getCvCategoriesIdsByCvId, nativeQuery = true)
    List<Integer> findCategoryIdsByCvId(Integer cvId);
}
