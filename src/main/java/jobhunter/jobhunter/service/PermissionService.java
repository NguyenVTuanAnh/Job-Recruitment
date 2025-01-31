package jobhunter.jobhunter.service;

import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.dto.permission.PermissionConverter;
import jobhunter.jobhunter.dto.permission.PermissionRequest;
import jobhunter.jobhunter.dto.permission.PermissionResponse;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionConverter permissionConverter;

    public boolean isPermissionExist(PermissionRequest permissionRequest) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                permissionRequest.getName(),
                permissionRequest.getApiPath(),
                permissionRequest.getMethod()
        );
    }
    public Optional<Permission> findPermissionById(long id){
        return permissionRepository.findById(id);
    }

    public List<Permission> findPermissionIn(List<Long> listId){
        return permissionRepository.findByIdIn(listId);
    }


    public PermissionResponse handleCreatePermission(PermissionRequest permissionRequest) {
        Permission permission = permissionConverter.toPermission(permissionRequest);
        return permissionConverter.toPermissionResponse(permissionRepository.save(permission));
    }

    public PermissionResponse handleUpdatePermission(PermissionRequest permissionRequest) {
        Optional<Permission> permissionOptional = this.findPermissionById(permissionRequest.getId());
        if (!permissionOptional.isEmpty()){
            Permission permission = permissionOptional.get();
            permission.setApiPath(permissionRequest.getApiPath());
            permission.setName(permissionRequest.getName());
            permission.setMethod(permissionRequest.getMethod());
            return permissionConverter.toPermissionResponse(permissionRepository.save(permission));
        }
        return null;
    }


    public ResultPagination handleGetPermissions(
            Pageable pageable, Specification<Permission> specification
    ) {
        Page<Permission> permissionPage = permissionRepository.findAll(specification, pageable);
        List<PermissionResponse> permissionResponses = permissionPage.getContent().stream()
                .map(permission -> permissionConverter.toPermissionResponse(permission)).toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber() + 1)              // curent page
                        .pageSize(pageable.getPageSize())        // amount page
                        .pages(permissionPage.getTotalPages())                 // size of a page
                        .total(permissionPage.getTotalElements())        // total elements
                        .build())
                .result(permissionResponses)
                .build();
    }


    public void handleDeletePermission(long id) {
        // delete in table permission_role
        Optional<Permission> permissionOptional = this.findPermissionById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete in table permission
        permissionRepository.delete(currentPermission);
    }
}
