package jobhunter.jobhunter.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.enumClass.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private long id;
    @NotBlank
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;
    @NotBlank
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Role role;
    private CompanyUser company;
    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
