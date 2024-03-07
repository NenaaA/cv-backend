package wisteria.cvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wisteria.cvapp.common.QueryConstants;
import wisteria.cvapp.model.Cv;


import java.util.List;

public interface CvRepository extends JpaRepository<Cv, Integer> {

    @Query(value = QueryConstants.getAllCvForUserId, nativeQuery = true)
    List<Cv> getAllCvForUserId(Integer userId);

    List<Cv> findAllByUser_Id(Integer userId);

//    @Query(value = QueryConstants.getCvDetails, nativeQuery = true)
//    List<List<String>> getCvDetails(Integer cvId, Integer userId);

    @Query(value = QueryConstants.getCvById, nativeQuery = true)
    Cv getCvById(Integer cvId);

}
