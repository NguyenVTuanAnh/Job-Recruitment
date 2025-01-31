package jobhunter.jobhunter.dto.subscriber;

import jobhunter.jobhunter.domain.Subscriber;
import jobhunter.jobhunter.dto.email.JobEmailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriberConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Subscriber toSubscriber(SubscriberRequest subscriberRequest) {
        return modelMapper.map(subscriberRequest, Subscriber.class);
    }

    public SubscriberResponse toSubscriberResponse(Subscriber subscriber) {
        SubscriberResponse subscriberResponse = modelMapper.map(subscriber, SubscriberResponse.class);

        return subscriberResponse;
    }


}
