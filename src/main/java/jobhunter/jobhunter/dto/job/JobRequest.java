package jobhunter.jobhunter.dto.job;

import jakarta.validation.constraints.NotBlank;
import jobhunter.jobhunter.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private Double salary;
    private Integer quantity;
    private String level;
    private String description;
    private boolean active;
    private Instant startDate;
    private Instant endDate;
    private List<Skill> skills;
}
