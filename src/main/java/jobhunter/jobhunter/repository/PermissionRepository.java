package jobhunter.jobhunter.repository;

import jobhunter.jobhunter.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> , JpaSpecificationExecutor<Permission> {
    boolean existsByModuleAndApiPathAndMethod(String name, String apiPath, String method);
    Optional<Permission> findById(long id);
    List<Permission> findByIdIn(List<Long> listId);
}

