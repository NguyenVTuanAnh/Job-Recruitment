package jobhunter.jobhunter.repository;

import jobhunter.jobhunter.domain.Subscriber;
import jobhunter.jobhunter.dto.subscriber.SubscriberRequest;
import jobhunter.jobhunter.dto.subscriber.SubscriberResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);
}
