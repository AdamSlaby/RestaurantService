package pl.restaurant.orderservice.business.service.payment;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.OrderServiceApplication;
import pl.restaurant.orderservice.business.service.order.OnlineOrderService;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaypalServiceImpl implements PaypalService {
    private final String SUCCESS_PAYMENT_URL = OrderServiceApplication.MAIN_SITE + "/pay/success?id=";
    private final String CANCEL_PAYMENT_URL = OrderServiceApplication.MAIN_SITE + "/pay/cancel?id=";
    private OnlineOrderService orderService;
    private APIContext apiContext;
    @Override
    public Payment createPayment(Long orderId) throws PayPalRESTException {
        OnlineOrderEntity order = orderService.getOrder(orderId);

        Amount amount = new Amount();
        amount.setCurrency("PLN");
        amount.setTotal(order.getPrice().toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(orderService.getOrderDescription(order));
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_PAYMENT_URL + orderId);
        redirectUrls.setReturnUrl(SUCCESS_PAYMENT_URL + orderId);
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
