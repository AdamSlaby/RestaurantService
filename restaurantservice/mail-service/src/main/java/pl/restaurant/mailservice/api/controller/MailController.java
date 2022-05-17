package pl.restaurant.mailservice.api.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.mailservice.api.request.MailInfo;
import pl.restaurant.mailservice.business.exception.CannotSendMailException;
import pl.restaurant.mailservice.business.service.MailSender;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@Log4j2
@CrossOrigin
public class MailController {

    @Value("${spring.mail.username}")
    private String restaurantEmail;
    private MailSender mailSender;

    public MailController(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/")
    public void sendMail(@RequestBody @Valid MailInfo mailInfo) {
        StringBuilder builder = new StringBuilder(mailInfo.getContent());
        builder.append("\nImię wysyłającego maila: ").append(mailInfo.getName());
        builder.append("\nEmail nadawcy: ").append(mailInfo.getEmail());
        builder.append("\nNumer telefonu nadawcy: ").append(mailInfo.getPhoneNumber());
        try {
            mailSender.sendMail(restaurantEmail, mailInfo.getSubject(), builder.toString(), false);
        } catch (MessagingException ex) {
            log.log(Level.ERROR, ex.getMessage());
            throw new CannotSendMailException();
        }
    }
}
