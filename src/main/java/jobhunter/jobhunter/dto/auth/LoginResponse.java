package jobhunter.jobhunter.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jobhunter.jobhunter.domain.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;
    @Getter
    @Setter
    @Builder
    public static class UserLogin {
        private long id;
        private String email;
        private String username;
        private Role role;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String email;
        private String name;
    }


}
