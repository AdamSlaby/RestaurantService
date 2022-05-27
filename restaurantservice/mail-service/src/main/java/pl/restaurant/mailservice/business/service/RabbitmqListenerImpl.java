package pl.restaurant.mailservice.business.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.restaurant.mailservice.api.request.OrderEmailInfo;
import pl.restaurant.mailservice.api.request.ReservationEmailInfo;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
@Log4j2
public class RabbitmqListenerImpl implements RabbitmqListener{
    private MailSender mailSender;

    @RabbitListener(queues = "reservation")
    @Override
    public void receiveReservationMessage(ReservationEmailInfo info) {
        log.log(Level.INFO, info);
        try {
            InputStream stream = new ClassPathResource("static/restaurantIcon.jpg").getInputStream();
            mailSender.sendMail(info.getEmail(), MailSenderImpl.RESERVATION_CONFIRMATION,
                    MailSenderImpl.getContentForReservationConfirmation(info), true, stream.readAllBytes());
        } catch (MessagingException | IOException ex) {
            log.log(Level.ERROR, ex.getMessage());
        }
    }

    @RabbitListener(queues = "order")
    @Override
    public void receiveOrderMessage(OrderEmailInfo info) {
        log.log(Level.INFO, info);
        try {
            InputStream stream = new ClassPathResource("static/restaurantIcon.jpg").getInputStream();
            mailSender.sendMail(info.getEmail(), MailSenderImpl.ORDER_CONFIRMATION,
                    MailSenderImpl.getContentForOrderConfirmation(info), true, stream.readAllBytes());
        } catch (MessagingException | IOException ex) {
            log.log(Level.ERROR, ex.getMessage());
        }
    }
}
