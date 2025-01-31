package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.role.RoleConverter;
import jobhunter.jobhunter.dto.role.RoleRequest;
import jobhunter.jobhunter.dto.role.RoleResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleConverter roleConverter;

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest roleRequest) {
        if (roleService.existByName(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLENAME_EXISTED);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<RoleResponse>builder()
                        .error(null)
                        .message("role created successfully")
                        .statusCode("201")
                        .data(roleService.handleCreateRole(roleRequest))
                        .build());
    }


    @PutMapping("/roles")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@RequestBody RoleRequest roleRequest) {
        if (roleService.existByName(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLENAME_EXISTED);
        }
        if (roleService.findById(roleRequest.getId()).isEmpty()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<RoleResponse>builder()
                        .error(null)
                        .message("role updated successfully")
                        .statusCode("200")
                        .data(roleService.handleUpdateRole(roleRequest))
                        .build());

    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable("id") long id){
        Optional<Role> roleOptional = roleService.findById(id);
        if(roleOptional.isEmpty()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<RoleResponse>builder()
                        .data(roleConverter.toRoleResponse(roleOptional.get()))
                        .message("get role successfuly")
                        .error(null)
                        .statusCode("200")
                        .build());
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<ResultPagination>> getRoles(
            Pageable pageable, @Filter Specification<Role> spec
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .message("get permissions")
                        .error(null)
                        .data(roleService.handleGetRoles(pageable, spec))
                        .build());
    }



    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable("id") long id) {
        if (roleService.findById(id).isEmpty()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        roleService.handleDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .error(null)
                        .message("role deleted successfully")
                        .statusCode("200")
                        .build());
    }
}
