package jobhunter.jobhunter.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultPagination {
    private Meta meta;
    private Object result;
}
