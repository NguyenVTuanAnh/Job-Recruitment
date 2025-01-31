package jobhunter.jobhunter.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {
    private long id;
    @NotNull
    @NotBlank(message = "NOT_BLANK")
    private String name;
    @NotNull(message = "not blank ")
    private String description;
    @NotNull(message = "not blank ")
    private String address;
    private String logo;
}
