package wisteria.cvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wisteria.cvapp.common.QueryConstants;
import wisteria.cvapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value= QueryConstants.getUserById,nativeQuery = true)
    User getUserById(Integer id);
    User findByUsername(String username);
    User findByEmail(String email);

}
