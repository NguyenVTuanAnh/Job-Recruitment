package jobhunter.jobhunter.dto.skill;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SkillRequest {
    private long id;
    @NotBlank(message = "NOT_BLANK")
    private String name;

}
