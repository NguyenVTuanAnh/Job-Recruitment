package jobhunter.jobhunter.service;

import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.dto.permission.PermissionConverter;
import jobhunter.jobhunter.dto.permission.PermissionResponse;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.dto.role.RoleConverter;
import jobhunter.jobhunter.dto.role.RoleRequest;
import jobhunter.jobhunter.dto.role.RoleResponse;
import jobhunter.jobhunter.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleConverter roleConverter;



    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Optional<Role> findById(long id) {
        return roleRepository.findById(id);
    }

    public RoleResponse handleCreateRole(RoleRequest roleRequest) {
        if (roleRequest.getPermissions() != null){
            List<Long> listId = roleRequest.getPermissions().stream()
                    .map(p -> p.getId()).toList();
            List<Permission> permissions = permissionService.findPermissionIn(listId);
            roleRequest.setPermissions(permissions);
        }
        Role role = roleConverter.toRole(roleRequest);
        return roleConverter.toRoleResponse(roleRepository.save(role));
    }

    public RoleResponse handleUpdateRole(RoleRequest roleRequest) {
        Role role = this.findById(roleRequest.getId()).get();
        if (roleRequest.getPermissions() != null){
            List<Long> listId = roleRequest.getPermissions().stream()
                    .map(p -> p.getId()).toList();
            List<Permission> permissions = permissionService.findPermissionIn(listId);
            role.setPermissions(permissions);
        }
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        role.setActive(roleRequest.isActive());
        return roleConverter.toRoleResponse(roleRepository.save(role));
    }

    public ResultPagination handleGetRoles(Pageable pageable, Specification<Role> spec){
        Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        List<Role> tmp = rolePage.getContent();
        List<RoleResponse> roleResponses = rolePage.getContent().stream()
                .map(role -> roleConverter.toRoleResponse(role)).toList();
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .pages(rolePage.getTotalPages())
                        .total(rolePage.getTotalElements())
                        .build())
                .result(roleResponses)
                .build();

    }

    public void handleDeleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
