package pl.restaurant.mailservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.restaurant.mailservice.api.request.OrderEmailInfo;
import pl.restaurant.mailservice.api.request.OrderInfo;
import pl.restaurant.mailservice.api.request.ReservationEmailInfo;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@AllArgsConstructor
public class MailSenderImpl implements MailSender {
    private JavaMailSender javaMailSender;

    public static final String RESERVATION_CONFIRMATION = "Potwierdzenie dokonania rezerwacji";
    public static final String ORDER_CONFIRMATION = "Dziękujemy za zamówienie w Restaurant";

    @Override
    public void sendMail(String addressee, String subject, String content, boolean isHtmlContent)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(addressee);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendMail(String addressee, String subject, String content, boolean isHtmlContent, byte[] image)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(addressee);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, isHtmlContent);
        mimeMessageHelper.addInline("restaurantIcon",  new ByteArrayResource(image){
            @Override
            public String getFilename() {
                return "restaurantIcon.jpg";
            }
        });
        javaMailSender.send(mimeMessage);
    }

    public static String getContentForReservationConfirmation(ReservationEmailInfo info) {
        return "<div style=\"width: 100%; height: 100%; background-color: #333333; min-height:900px\"> \n" +
                "         <div style=\"font-size: 25px; text-align: center;padding: 1.5rem 2rem;\">\n" +
                "           <img src=\"cid:restaurantIcon\" alt=\"restaurant-icon\"/>\n" +
                "        </div>\n" +
                "         <div style=\"text-align: center; background-color: white; color: #333333; padding: 5rem 1rem;width: 30%;margin: auto;min-width: 400px\">\n" +
                "             <h2>Hej " + info.getName() + " " + info.getSurname() + "</h2>\n" +
                "           <div style=\"margin-bottom: 2rem; align-content: center; padding: 0.5rem 2rem; text-align: left;font-size: 18px;\">\n" +
                "               <p style=\"text-align: justify;\">\n" +
                "                Dziękujemy za dokonanie rezerwacji w naszej restauracji Restaurant.\n" +
                "                Poniżej znajdują się informacje na temat samej rezerwacji. \n" +
                "               </p>\n" +
                "           </div>\n" +
                "          <div style=\"height: auto; width: auto;font-size: 1rem;\">\n" +
                "                <p>\n" +
                "                Identyfikator rezerwacji:  " + info.getId() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Adres restauracji:  " + info.getRestaurantInfo() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Imię i nazwisko rezerwującego: " + info.getName() + " " + info.getSurname() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Numer telefonu: " + info.getPhoneNr() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Data i godzina rozpoczęcia rezerwacji: " + info.getFromHour().format(formatter) + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Data i godzina zakończenia rezerwacji: " + info.getToHour().format(formatter) + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Numery stolików/stolika: " + info.getTableIds().toString() + "\n" +
                "                </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    public static String getContentForOrderConfirmation(OrderEmailInfo info) {
        return "<div style=\"width: 100%; height: 100%; background-color: #333333; min-height:900px\"> \n" +
                "         <div style=\"font-size: 25px; text-align: center;padding: 1.5rem 2rem;\">\n" +
                "           <img src=\"restaurantIcon.jpg\" alt=\"restaurant-icon\"/>\n" +
                "        </div>\n" +
                "         <div style=\"text-align: center; background-color: white; color: #333333; padding: 5rem 1rem;width: 30%;margin: auto;min-width: 400px\">\n" +
                "             <h2>Hej " + info.getName() + " " + info.getSurname() + "</h2>\n" +
                "             <div style=\"margin-bottom: 1em; align-content: center; padding: 0.5rem 2rem; text-align: left;font-size: 18px;\">\n" +
                "               <p style=\"text-align: justify;\">\n" +
                "                Dziękujemy za dokonanie zamówienia w naszej restauracji Restaurant.\n" +
                "                Poniżej znajdują się informacje na temat zamówienia. \n" +
                "               </p>\n" +
                "       </div>\n" +
                "       <div style=\"height: auto; width: auto;font-size: 1rem;text-align: left;padding-left: 2rem;margin-bottom: 1.5rem;\">\n" +
                "       <h3>Informacje o zamówieniu:</h3>\n" +
                "                <p>\n" +
                "                Identyfikator zamówienia:  " + info.getId() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Adres restauracji:  " + info.getRestaurantInfo().toString() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Imię i nazwisko rezerwującego: " + info.getName() + " " + info.getSurname() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Numer telefonu: " + info.getPhoneNr() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Adres dostawy: " + info.getAddress().toString() + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Piętro: " + (info.getFloor() == null ? "Brak" : info.getFloor())  + "\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                Sposób płatności: " + info.getPaymentMethod() + " \n" +
                "                </p>\n" +
                "      </div>\n" +
                "      <hr>\n" +
                "       <div style=\"height: auto; width: auto;font-size: 1rem;text-align: left;padding: 0 2rem;\">\n" +
                "       <h3 style=\"justify-content: center;\">Zamówienie:</h3>\n" +
                convertOrdersToString(info.getOrders()) +
                "       <div style=\"margin: 1em 0\">\n" +
                "           <div style=\"float: left;\">Razem</div>  <div style=\"float: right;\">" + info.getPrice().toString() + " zł</div> \n" +
                "           <div style=\"clear: both;\"></div>\n" +
                "       </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    private static String convertOrdersToString(List<OrderInfo> orders) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < orders.size(); i++) {
            OrderInfo info = orders.get(i);
            builder.append(" <div style=\"margin: 1em 0\">\n" + "<div style=\"float: left;\">")
                    .append(i + 1).append(". ")
                    .append(info.getName())
                    .append("</div>  ")
                    .append("<div style=\"float: right;\">")
                    .append(info.getPrice().toString())
                    .append(" zł</div> \n")
                    .append("<div style=\"clear: both;\"></div>\n")
                    .append("</div>\n");
        }
        return builder.toString();
    }
}
