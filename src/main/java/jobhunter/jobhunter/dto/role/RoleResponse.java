package jobhunter.jobhunter.dto.role;

import jakarta.validation.constraints.NotBlank;
import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.dto.permission.PermissionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private long id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<Permission> permissions;
}
