package jobhunter.jobhunter.dto.email;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEmailResponse {
    private String jobName;
    private String companyName;
    private double salary;
    private List<String> skillName;
}
