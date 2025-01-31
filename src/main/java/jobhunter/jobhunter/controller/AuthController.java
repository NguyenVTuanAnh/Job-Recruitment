package jobhunter.jobhunter.controller;

import jakarta.validation.Valid;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.dto.auth.LoginDTO;
import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.auth.LoginResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.AuthService;
import jobhunter.jobhunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Value("${expiration-refresh}")
    private long refreshExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>>  handleLogin(@RequestBody @Valid LoginDTO loginDTO){

        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.handleGetUserByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserLogin userLogin = LoginResponse.UserLogin.builder()
                    .username(user.getUsername())
                    .email(user.getUsername())
                    .role(user.getRole())
                    .id(user.getId())
                    .build();
        loginResponse.setUser(userLogin);

        // create access token
        String accessToken = authService.createAccessToken(authentication, loginResponse);
        loginResponse.setAccessToken(accessToken);
        // create refresh token
        String refreshToken = authService.createRefreshToken(authentication, loginResponse);
        userService.handleUpdateRefreshToken(refreshToken, user);
        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpiration)
                .build();


        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(ApiResponse.<LoginResponse>builder()
                        .statusCode("200")
                        .message("login successfully")
                        .data(loginResponse)
                        .build());
    }

    // get current user is logining
    @GetMapping("/auth/account")
    public ResponseEntity<ApiResponse<LoginResponse.UserGetAccount>> getCurrentUserLogin(){
        String username = authService.getCurrentUserLogin().isPresent() ? authService.getCurrentUserLogin().get() : null;
        User user = userService.handleGetUserByUsername(username);
        LoginResponse.UserLogin userLogin = LoginResponse.UserLogin.builder()
                .email(user.getUsername())
                .username(user.getUsername())
                .id(user.getId())
                .build();
        LoginResponse.UserGetAccount res = new LoginResponse.UserGetAccount();
        res.setUser(userLogin);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<LoginResponse.UserGetAccount>builder()
                        .statusCode("200")
                        .message("get current user login")
                        .data(res)
                        .build());
    }


    @GetMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUserRefreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ){
        // check valid token
        Jwt decodedToken = authService.validRefreshToken(refreshToken);
        String username = decodedToken.getSubject();

        // check email + refresh token in database
        User user = userService.handleGetUserByUsernameAndRefreshToken(username, refreshToken);
        if (user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        LoginResponse loginResponse = new LoginResponse();

        // create new  access token
        LoginResponse.UserLogin userLogin = LoginResponse.UserLogin.builder()
                .email(user.getUsername())
                .username(user.getUsername())
                .id(user.getId())
                .build();
        loginResponse.setUser(userLogin);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = authService.createAccessToken(authentication, loginResponse);
        // create new refresh token and save
        String newRefreshToken = authService.createRefreshToken(authentication, loginResponse);
        userService.handleUpdateRefreshToken(refreshToken,user);
        // set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpiration)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.<LoginResponse>builder()
                        .statusCode("200")
                        .message("refresh successfully")
                        .error(null)
                        .data(loginResponse)
                        .build());

    }
    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void>>  handleLogout(){
        String username = authService.getCurrentUserLogin().isPresent()
                ? authService.getCurrentUserLogin().get() : null;
        userService.handleUpdateRefreshToken(null,userService.handleGetUserByUsername(username));
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.<Void>builder()
                        .statusCode("200")
                        .message("logout successfully")
                        .build());
    }
}
