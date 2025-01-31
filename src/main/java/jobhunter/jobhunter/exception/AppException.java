package jobhunter.jobhunter.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// a custom exception
public class AppException extends RuntimeException{

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
