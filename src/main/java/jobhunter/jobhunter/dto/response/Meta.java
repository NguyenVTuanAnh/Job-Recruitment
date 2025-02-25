package jobhunter.jobhunter.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
