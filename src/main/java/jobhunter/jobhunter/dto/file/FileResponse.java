package jobhunter.jobhunter.dto.file;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {
    private String fileName;
    private Instant updatedAt;
}
