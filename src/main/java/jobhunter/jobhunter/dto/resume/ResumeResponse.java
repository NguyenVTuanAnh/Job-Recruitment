package jobhunter.jobhunter.dto.resume;

import jobhunter.jobhunter.dto.job.JobResponse;
import jobhunter.jobhunter.dto.user.UserResponse;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeResponse {
    private Long id;
    private String email;
    private String url;
    private String status;
    private JobResponse job;
    private UserResponse user;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
