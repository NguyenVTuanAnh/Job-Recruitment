package jobhunter.jobhunter.dto.role;

import jakarta.validation.constraints.NotBlank;
import jobhunter.jobhunter.domain.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private long id;
    @NotBlank(message = "NOT_BLANK")
    private String name;
    private String description;
    private boolean active;
    private List<Permission> permissions;
}
