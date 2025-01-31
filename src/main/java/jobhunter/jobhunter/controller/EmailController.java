package jobhunter.jobhunter.controller;


import jobhunter.jobhunter.service.EmailService;
import jobhunter.jobhunter.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {




    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/email")
    //@Scheduled(cron = "*/10 * * * * *")
    public String sendSimpleEmail() {
        subscriberService.sendSubscribersEmailJobs();
        return "Hello World";
    }
}
