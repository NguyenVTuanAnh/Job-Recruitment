package jobhunter.jobhunter.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorCode {
    AUTHORLESS("400","you are not authorized"),
    ROLENAME_EXISTED("400", "name of role existed"),
    PERMISSION_EXISTED("400", "Permission already existed"),
    FILE_INVALID("400", "file invalid"),
    USER_NOT_FOUND("400", "user not found with username and refresh token"),
    ID_NOT_FOUND("400", "id not found"),
    INPUT_MISSING("400", "input missing"),
    NOT_BLANK("400","not blank"),
    USERNAME_INVALID("400","username must be greater than 3 characters"),
    PASSWORD_INVALID("400","password must be greater than 6 characters"),
    USERNAME_EXISTED("400","username already exists"),
    UNCATEGORIZED_EXCEPTION("500","uncategorized error")
    ;
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
