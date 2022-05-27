package pl.restaurant.orderservice.business.service.payment;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface PayuService {
    String createPayment(Long orderId);
}
