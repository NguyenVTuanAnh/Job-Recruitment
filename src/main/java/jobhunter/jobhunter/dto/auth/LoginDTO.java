package jobhunter.jobhunter.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotNull
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;
    @NotNull
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
}
