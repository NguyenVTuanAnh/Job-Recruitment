package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.dto.user.UserRequest;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.user.UserResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if(userService.handleGetUserByUsername(userRequest.getUsername()) != null){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .message("User created")
                        .statusCode("200")
                        .error(null)
                        .data(userService.handleCreateUser(userRequest))
                        .build());

    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .message("User deleted")
                        .statusCode("200")
                        .data(null)
                        .build());
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<UserResponse>builder()
                        .message("User found")
                        .statusCode("200")
                        .error(null)
                        .data(userService.fetchUserById(id))
                        .build()
        );
    }

    // fetch all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<ResultPagination>> getAllUser(
           @Filter Specification<User> spec,
           Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .statusCode("200")
                        .message("success")
                        .error(null)
                        .data(userService.fetchAllUser(spec,pageable))
                        .build());
    }

    // update user
    @PutMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<UserResponse>builder()
                .message("User updated")
                .error(null)
                .statusCode("200")
                .data(userService.handleUpdateUser(userRequest))
                .build());
    }
}
