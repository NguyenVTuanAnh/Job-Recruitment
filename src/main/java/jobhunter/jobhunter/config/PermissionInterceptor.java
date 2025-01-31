package jobhunter.jobhunter.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jobhunter.jobhunter.domain.Permission;
import jobhunter.jobhunter.domain.Role;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.AuthService;
import jobhunter.jobhunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

// custom intercepter
@Transactional
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    // before controller
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ){

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String username = AuthService.getCurrentUserLogin().isPresent() ?
                AuthService.getCurrentUserLogin().get() : "";
        if (username != null && !username.isEmpty()) {
            User user = userService.handleGetUserByUsername(username);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissionList = role.getPermissions();
                    boolean isAllow = permissionList.stream().anyMatch(
                            permission -> permission.getApiPath().equals(path)
                                    && permission.getMethod().equals(httpMethod)
                    );

                    if (!isAllow){
                        throw new AppException(ErrorCode.AUTHORLESS);
                    }
                } else {
                    throw new AppException(ErrorCode.AUTHORLESS);
                }
            }
        }

        return true;
    }
}