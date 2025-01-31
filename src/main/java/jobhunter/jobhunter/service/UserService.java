package jobhunter.jobhunter.service;

import jakarta.validation.Valid;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.dto.user.UserConverter;
import jobhunter.jobhunter.domain.Company;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.dto.user.UserRequest;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.user.UserResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyService companyService;

    public UserResponse handleCreateUser(@Valid UserRequest userRequest) {
        User user = userConverter.toUser(userRequest);
        if (userRequest.getCompany() != null){
            Optional<Company> company = companyService.handleGetCompanyById(userRequest.getCompany().getId());
            user.setCompany(company.isPresent() ? company.get() : null);
        }

        if (roleService.findById(userRequest.getRole().getId()).isPresent()){
            Optional<Role> role = roleService.findById(userRequest.getRole().getId());
            user.setRole(role.get());
        }
        userRepository.save(user);
        return userConverter.toUserResponse(user);
    }

    public void handleDeleteUser(long id) {
        if(!userRepository.existsById(id)) throw new AppException(ErrorCode.ID_NOT_FOUND);
        userRepository.deleteById(id);
    }

    public UserResponse fetchUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw  new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return userConverter.toUserResponse(user);
    }

    public ResultPagination fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> users = userRepository.findAll(spec,pageable);
        List<UserResponse> userResponseList = users.getContent().stream().map(user -> userConverter.toUserResponse(user))
                .toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .total(users.getTotalElements())
                        .pages(users.getTotalPages())
                        .build())
                .result(userResponseList)
                .build();
    }

    public UserResponse handleUpdateUser(UserRequest userRequest) {
        User currentUser = userRepository.findById(userRequest.getId()).orElse(null);
        if (currentUser != null) {
            currentUser.setAddress(userRequest.getAddress());
            currentUser.setGender(userRequest.getGender());
            currentUser.setAge(userRequest.getAge());
            currentUser.setEmail(userRequest.getEmail());
        }
        if (userRequest.getCompany() != null){
            Optional<Company> company = companyService.handleGetCompanyById(userRequest.getCompany().getId());
            currentUser.setCompany(company.isPresent() ? company.get() : null);
        }

        if(userRequest.getRole() != null){
            Role currentRole = roleService.findById(userRequest.getRole().getId()).orElse(null);
            currentUser.setRole(currentRole);
        }
        userRepository.save(currentUser);
        return userConverter.toUserResponse(currentUser);
    }


    public User handleGetUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void handleUpdateRefreshToken(String refreshToken, User user) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public User handleGetUserByUsernameAndRefreshToken(String username, String refreshToken) {
        return userRepository.findByUsernameAndRefreshToken(username, refreshToken);
    }
}
