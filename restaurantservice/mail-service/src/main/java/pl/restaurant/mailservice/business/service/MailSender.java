package pl.restaurant.mailservice.business.service;

import javax.mail.MessagingException;
import java.time.format.DateTimeFormatter;

public interface MailSender {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    void sendMail(String addressee, String subject, String content, boolean isHtmlContent,  byte[] image)
            throws MessagingException;

    void sendMail(String addressee, String subject, String content, boolean isHtmlContent) throws MessagingException;
}
