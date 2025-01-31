package jobhunter.jobhunter.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jobhunter.jobhunter.domain.Job;
import jobhunter.jobhunter.domain.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JobService jobService;

    // send simple email
    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("nguyentuananhaz99@gmail.com");
        message.setSubject("hellkooooo");
        message.setText("heloo");
        mailSender.send(message);
    }
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }
    // send template email
    @Async          //(asynchronous) mutil thread/ xử lý bất đồng bộ
    public void sendEmailFromTemplateSync(
            String to,
            String subject,
            String templateName,
            Object value
    ) {
        Context context = new Context();
        List<Job> jobs = jobService.handleGetAllJob();
        context.setVariable("jobs", value);  // context of thymleaf
        context.setVariable("username", subject);
        // templateEngine is used to convert from HTML to string
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }
}
