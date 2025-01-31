package jobhunter.jobhunter.config;

import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.enumClass.GenderEnum;
import jobhunter.jobhunter.repository.PermissionRepository;
import jobhunter.jobhunter.repository.RoleRepository;
import jobhunter.jobhunter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@Transactional
public class ApplicationConfiguration {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    // khởi tạo 1 tai khoản admin ngay khi vừa khởi tạo lên
    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            if  (userRepository.findByUsername("admin") == null) {

                long numberOfPermissions = permissionRepository.count();
                if (numberOfPermissions == 0) {
                    List<Permission> arr = new ArrayList<>();
                    arr.add(new Permission("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
                    arr.add(new Permission("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
                    arr.add(new Permission("Delete a company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
                    arr.add(new Permission("Get a company by id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
                    arr.add(new Permission("Get companies with pagination", "/api/v1/companies", "GET", "COMPANIES"));

                    arr.add(new Permission("Create a job", "/api/v1/jobs", "POST", "JOBS"));
                    arr.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
                    arr.add(new Permission("Delete a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
                    arr.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
                    arr.add(new Permission("Get jobs with pagination", "/api/v1/jobs", "GET", "JOBS"));

                    arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
                    arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
                    arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
                    arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
                    arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

                    arr.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
                    arr.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
                    arr.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
                    arr.add(new Permission("Get a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
                    arr.add(new Permission("Get resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));

                    arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
                    arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
                    arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
                    arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
                    arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));

                    arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
                    arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
                    arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
                    arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
                    arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));

                    arr.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
                    arr.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
                    arr.add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
                    arr.add(new Permission("Get a subscriber by id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
                    arr.add(new Permission("Get subscribers with pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));

                    arr.add(new Permission("Download a file", "/api/v1/files", "POST", "FILES"));
                    arr.add(new Permission("Upload a file", "/api/v1/files", "GET", "FILES"));

                    this.permissionRepository.saveAll(arr);
                }

                long numberOfRoles = roleRepository.count();
                if (numberOfRoles == 0) {
                    Role role = Role.builder()
                            .permissions(permissionRepository.findAll())
                            .name("ADMIN")
                            .active(true)
                            .description("Administrator")
                            .build();
                    roleRepository.save(role);
                }
                long numberOfUsers = userRepository.count();

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456"))
                        .age(20)
                        .address("ha noi")
                        .gender(GenderEnum.MALE)
                        .role(roleRepository.findByName("ADMIN").isPresent() ?
                                roleRepository.findByName("ADMIN").get() : null)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
