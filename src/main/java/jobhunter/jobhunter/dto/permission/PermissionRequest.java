package jobhunter.jobhunter.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {
    private long id;
    @NotBlank(message = "method not blank")
    private String name;
    @NotBlank(message = "method not blank")
    private String apiPath;
    @NotBlank(message = "method not blank")
    private String method;
    @NotBlank(message = "module not blank")
    private String module;
}
