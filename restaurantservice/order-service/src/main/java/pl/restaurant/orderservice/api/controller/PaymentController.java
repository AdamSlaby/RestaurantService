package pl.restaurant.orderservice.api.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.orderservice.OrderServiceApplication;
import pl.restaurant.orderservice.api.request.payu.Notification;
import pl.restaurant.orderservice.business.exception.PaypalErrorException;
import pl.restaurant.orderservice.business.service.order.OnlineOrderService;
import pl.restaurant.orderservice.business.service.payment.PaypalService;
import pl.restaurant.orderservice.business.service.payment.PayuService;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@AllArgsConstructor
public class PaymentController {
    private PaypalService paypalService;
    private PayuService payUService;
    private OnlineOrderService orderService;

    @GetMapping("/pay/paypal/{id}")
    public String payByPayPal(@PathVariable("id") Long orderId) {
        try {
            Payment payment = paypalService.createPayment(orderId);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
            return "";
        } catch (PayPalRESTException ex) {
            throw new PaypalErrorException(ex.getMessage());
        }
    }

    @CrossOrigin(origins = "https://www.sandbox.paypal.com/**")
    @GetMapping("/pay/cancel")
    public String cancelPay(@RequestParam("id") Long orderId,
                            HttpServletResponse httpServletResponse) {
        orderService.rollbackOrder(orderId);
        httpServletResponse.setHeader("Location", OrderServiceApplication.FRONT_SITE);
        httpServletResponse.setStatus(302);
        return "redirect:localhost:4200/";
    }

    @CrossOrigin(origins = "https://www.sandbox.paypal.com/**")
    @GetMapping("/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("token") String token,
                             @RequestParam("PayerID") String payerId,
                             @RequestParam("id") Long orderId,
                             HttpServletResponse httpServletResponse) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                orderService.changePaymentStatus(orderId);
            } else {
                orderService.rollbackOrder(orderId);
            }
            httpServletResponse.setHeader("Location", OrderServiceApplication.FRONT_SITE);
            httpServletResponse.setStatus(302);
            return "redirect:localhost:4200/";
        } catch (PayPalRESTException ex) {
            httpServletResponse.setHeader("Location", OrderServiceApplication.FRONT_SITE);
            httpServletResponse.setStatus(302);
        }
        return "redirect:localhost:4200/";
    }

    @GetMapping("/pay/payu/{id}")
    public String payByPayU(@PathVariable("id") Long orderId) {
        return payUService.createPayment(orderId);
    }

    @CrossOrigin(origins = {"185.68.14.10", "185.68.14.11", "185.68.14.12", "185.68.14.26", "185.68.14.27", "185.68.14.28"})
    @PostMapping(value = "/pay/payu/notify")
    public void notify(@RequestBody Notification notification,
                       @RequestParam("id") Long orderId) {
        if (notification.getOrder().getStatus().equals("COMPLETED"))
            orderService.changePaymentStatus(orderId);
        else if (notification.getOrder().getStatus().equals("CANCELED")) {
            orderService.rollbackOrder(orderId);
        }
    }
}
