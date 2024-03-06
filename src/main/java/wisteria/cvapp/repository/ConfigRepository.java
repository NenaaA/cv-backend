package wisteria.cvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wisteria.cvapp.common.QueryConstants;
import wisteria.cvapp.model.Config;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
    @Query(value = QueryConstants.getValuesByGroupAndAttribute, nativeQuery = true)
    List<String> getValuesByGroupAndAttribute(String groupValue, String attributeValue);

    @Query(value = QueryConstants.getConfigListByGroupAndAttribute, nativeQuery = true)
    List<Config> getConfigListByGroupAndAttribute(String groupValue, String attributeValue);
}
