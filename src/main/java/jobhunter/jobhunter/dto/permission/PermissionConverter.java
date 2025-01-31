package jobhunter.jobhunter.dto.permission;

import jobhunter.jobhunter.domain.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Permission toPermission(PermissionRequest permissionRequest){
        Permission permission = modelMapper.map(permissionRequest, Permission.class);
        return permission;
    }

    public PermissionResponse toPermissionResponse(Permission permission){
        PermissionResponse permissionResponse = modelMapper.map(permission, PermissionResponse.class);
        return permissionResponse;
    }
}
