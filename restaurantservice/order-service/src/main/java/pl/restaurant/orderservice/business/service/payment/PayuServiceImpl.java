package pl.restaurant.orderservice.business.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.orderservice.api.mapper.PayuMapper;
import pl.restaurant.orderservice.api.request.payu.PayuAuthResponse;
import pl.restaurant.orderservice.api.request.payu.PayuResponse;
import pl.restaurant.orderservice.api.response.payu.Payload;
import pl.restaurant.orderservice.business.service.order.OnlineOrderService;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

@Service
public class PayuServiceImpl implements PayuService {
    //change it every time when start ngrok
    private static final String NOTIFY_URL = "https://6762-31-178-44-241.eu.ngrok.io/pay/payu/notify?id=";

    @Value("${app.payu.posId}")
    private String posId;

    @Value("${app.payu.md5}")
    private String secondKey;

    @Value("${app.payu.clientId}")
    private String clientId;

    @Value("${app.payu.secret}")
    private String secret;

    private OnlineOrderService orderService;

    public PayuServiceImpl(OnlineOrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String createPayment(Long orderId) {
        OnlineOrderEntity order = orderService.getOrder(orderId);
        PayuAuthResponse payuResponse = getAccessToken();
        Payload data = PayuMapper.mapOrderToPayload(order, posId, NOTIFY_URL + orderId,
                orderService.getOrderDescription(order));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payuResponse.getAccess_token());
        HttpEntity<Payload> request = new HttpEntity<>(data, headers);
        PayuResponse payuDetails = restTemplate.postForObject("https://secure.snd.payu.com/api/v2_1/orders",
                request, PayuResponse.class);
        assert payuDetails != null;
        return payuDetails.getRedirectUri();
    }

    private PayuAuthResponse getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + secret, headers);
        return restTemplate.postForObject("https://secure.snd.payu.com/pl/standard/user/oauth/authorize",
                request, PayuAuthResponse.class);
    }
}
