package jobhunter.jobhunter.dto.resume;

import jakarta.persistence.*;
import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.enumClass.StatusEnum;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeRequest {
    private Long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private User user;
    private Job job;
}
