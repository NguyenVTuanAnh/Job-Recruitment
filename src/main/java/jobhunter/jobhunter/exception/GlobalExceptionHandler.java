package jobhunter.jobhunter.exception;

import jobhunter.jobhunter.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    // catch all exception
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handlingRuntimeException(RuntimeException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .message(e.getMessage())
                .statusCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    //catch custom exception
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<Object> handlingAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .message(errorCode.getMessage())
                .error(errorCode.getMessage())
                .statusCode(errorCode.getCode())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public  ResponseEntity<ApiResponse<Void>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .statusCode(errorCode.getCode())
                        .error(errorCode.getMessage())
                        .message(errorCode.getMessage())
                        .data(null)
                        .build());
    }
}
