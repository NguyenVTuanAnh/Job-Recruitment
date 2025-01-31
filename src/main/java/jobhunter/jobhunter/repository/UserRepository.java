package jobhunter.jobhunter.repository;

import jobhunter.jobhunter.domain.Company;
import jobhunter.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsById(long id);
    User findByUsernameAndRefreshToken(String username, String refreshToken);

    List<User> findByCompany(Company com);

    List<User> findByIdIn(List<Long> ids);
}
