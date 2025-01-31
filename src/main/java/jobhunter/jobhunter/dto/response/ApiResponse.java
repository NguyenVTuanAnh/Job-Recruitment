package jobhunter.jobhunter.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)  // nếu field nào null thì sẽ ko show ra
public class ApiResponse <T>{
    private String statusCode;
    private String message;
    private String error;
    private T data;
}
