package jobhunter.jobhunter.dto.role;

import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.dto.permission.PermissionConverter;
import jobhunter.jobhunter.dto.permission.PermissionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleConverter {

    @Autowired
    private ModelMapper modelMapper;



    public Role toRole(RoleRequest roleRequest) {
        Role role = modelMapper.map(roleRequest, Role.class);
        return role;
    }

    public RoleResponse toRoleResponse(Role role) {
        RoleResponse roleResponse = modelMapper.map(role, RoleResponse.class);
        return roleResponse;
    }
}
