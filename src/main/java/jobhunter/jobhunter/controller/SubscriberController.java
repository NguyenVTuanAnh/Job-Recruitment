package jobhunter.jobhunter.controller;

import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.subscriber.SubscriberRequest;
import jobhunter.jobhunter.dto.subscriber.SubscriberResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.repository.SubscriberRepository;
import jobhunter.jobhunter.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {



    @Autowired
    private SubscriberService subscriberService;



    @PostMapping("/subscribers")
    private ResponseEntity<ApiResponse<SubscriberResponse>> createSubscribe(@RequestBody SubscriberRequest subscriberRequest) {
        if (subscriberService.handleIsExistEmail(subscriberRequest.getEmail()) == true) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubscriberResponse>builder()
                        .error(null)
                        .message("created a subscriber")
                        .statusCode("201")
                        .data(subscriberService.handleCreateSubscriber(subscriberRequest))
                        .build());
    }

    @PutMapping("/subscribers")
    private ResponseEntity<ApiResponse<SubscriberResponse>> updateSubscribe(@RequestBody SubscriberRequest subscriberRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<SubscriberResponse>builder()
                        .error(null)
                        .message("updated a subscriber")
                        .statusCode("200")
                        .data(subscriberService.handleUpdateSubscriber(subscriberRequest))
                        .build());
    }

}
