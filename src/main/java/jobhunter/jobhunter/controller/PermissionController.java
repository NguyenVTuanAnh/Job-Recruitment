package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.dto.permission.PermissionConverter;
import jobhunter.jobhunter.dto.permission.PermissionRequest;
import jobhunter.jobhunter.dto.permission.PermissionResponse;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {


    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermissionConverter permissionConverter;

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        if (permissionService.isPermissionExist(permissionRequest)) {
            throw  new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PermissionResponse>builder()
                        .data(permissionService.handleCreatePermission(permissionRequest))
                        .message("created")
                        .statusCode("201")
                        .build());
    }

    @PutMapping("/permissions")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        if (permissionService.isPermissionExist(permissionRequest)) {
            throw  new AppException(ErrorCode.PERMISSION_EXISTED);
        }


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<PermissionResponse>builder()
                        .error(null)
                        .data(permissionService.handleUpdatePermission(permissionRequest))
                        .message("updated")
                        .statusCode("200")
                        .build());
    }


    @GetMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(@PathVariable long id) {
        Optional<Permission> permission = permissionService.findPermissionById(id);
        if (permission.isEmpty()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<PermissionResponse>builder()
                        .message("get permission")
                        .error(null)
                        .data(permissionConverter.toPermissionResponse(permission.get()))
                        .build());
    }


    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<ResultPagination>> getPermissions(
            Pageable pageable, @Filter Specification<Permission> spec
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .message("get permissions")
                        .error(null)
                        .data(permissionService.handleGetPermissions(pageable, spec))
                        .build());
    }


    @DeleteMapping("/permission/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable long id) {
        if (permissionService.findPermissionById(id).isEmpty()){
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .message("deleted permission successfully")
                        .error(null)
                        .data(null)
                        .statusCode("200")
                        .build());
    }
}
