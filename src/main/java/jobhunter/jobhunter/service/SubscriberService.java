package jobhunter.jobhunter.service;

import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.Skill;
import jobhunter.jobhunter.domain.Subscriber;
import jobhunter.jobhunter.dto.email.JobEmailResponse;
import jobhunter.jobhunter.dto.subscriber.SubscriberConverter;
import jobhunter.jobhunter.dto.subscriber.SubscriberRequest;
import jobhunter.jobhunter.dto.subscriber.SubscriberResponse;
import jobhunter.jobhunter.repository.SkillRepository;
import jobhunter.jobhunter.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SubscriberConverter subscriberConverter;
    @Autowired
    private JobService jobService;

    @Autowired
    private EmailService emailService;

    public boolean handleIsExistEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

    public SubscriberResponse handleCreateSubscriber(SubscriberRequest subscriberRequest) {
        Subscriber subscriber = subscriberConverter.toSubscriber(subscriberRequest);
        if(subscriberRequest.getSkills() != null && subscriberRequest.getSkills().size() > 0) {
            List<Long> requestSkill = subscriberRequest.getSkills()
                    .stream().map(skill -> skill.getId()).toList();
            List<Skill> skillList = skillRepository.findByIdIn(requestSkill);
            subscriber.setSkills(skillList);
        }
        return subscriberConverter.toSubscriberResponse(subscriberRepository.save(subscriber));
    }


    public SubscriberResponse handleUpdateSubscriber(SubscriberRequest subscriberRequest) {
        Subscriber subscriber = subscriberRepository.findById(subscriberRequest.getId()).orElse(null);
        if(subscriberRequest.getSkills() != null && subscriber != null) {
            List<Long> ids = subscriberRequest.getSkills().stream().map(skill -> skill.getId()).toList();
            List<Skill> skillList = skillRepository.findByIdIn(ids);
            subscriber.setSkills(skillList);
        }
        subscriberRepository.save(subscriber);
        return subscriberConverter.toSubscriberResponse(subscriber);
    }

    public JobEmailResponse converter(Job job){
        List<String> skillNames = job.getSkills().stream().map(skill -> skill.getName()).toList();
        return JobEmailResponse.builder()
                .jobName(job.getName())
                .companyName(job.getCompany().getName())
                .salary(job.getSalary())
                .skillName(skillNames)
                .build();
    }

    public void sendSubscribersEmailJobs(){
        List<Subscriber> subscribers = subscriberRepository.findAll();
        if (!subscribers.isEmpty() && subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                List<Skill> skills = subscriber.getSkills();
                if (!skills.isEmpty() && skills != null) {
                    List<Job> jobs = jobService.handleGetJobBySkillIn(skills);
                    if (!jobs.isEmpty() && jobs != null) {
                        List<JobEmailResponse> jobEmailResponses = jobs.stream()
                                .map(job -> this.converter(job)).toList();
                        emailService.sendEmailFromTemplateSync(
                                subscriber.getEmail(),
                                subscriber.getName(),
                                "job",
                                jobEmailResponses
                                );
                    }
                }


            }
        }
    }
}
