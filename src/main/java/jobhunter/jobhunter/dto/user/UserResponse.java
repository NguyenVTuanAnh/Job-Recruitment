package jobhunter.jobhunter.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.enumClass.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private long id;
    private String username;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createAt;
    private Instant updateAt;
    private Role role;
    private UserRequest.CompanyUser company;
    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
