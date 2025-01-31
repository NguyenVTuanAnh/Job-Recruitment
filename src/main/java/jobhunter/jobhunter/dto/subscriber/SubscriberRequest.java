package jobhunter.jobhunter.dto.subscriber;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jobhunter.jobhunter.dto.skill.SkillRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberRequest {
    private long id;
    @NotBlank(message = "NOT_BLANK")
    private String name;
    @NotBlank(message = "NOT_BLANK")
    private String email;
    private List<SkillRequest> skills;
}
