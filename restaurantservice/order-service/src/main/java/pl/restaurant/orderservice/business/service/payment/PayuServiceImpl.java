package pl.restaurant.orderservice.business.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.OrderServiceApplication;
import pl.restaurant.orderservice.api.mapper.PayuMapper;
import pl.restaurant.orderservice.api.request.payu.PayuAuthResponse;
import pl.restaurant.orderservice.api.request.payu.PayuResponse;
import pl.restaurant.orderservice.api.response.payu.Payload;
import pl.restaurant.orderservice.business.service.order.OnlineOrderService;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class PayuServiceImpl implements PayuService {
    private static final String NOTIFY_URL = OrderServiceApplication.MAIN_SITE + "/pay/payu/notify?id=";

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
        Client client = ClientBuilder.newClient();
        Payload data = PayuMapper.mapOrderToPayload(order, posId, NOTIFY_URL + orderId,
                orderService.getOrderDescription(order));
        Entity payload = Entity.json(mapPayloadToString(data));
        Response response = client.target("https://secure.snd.payu.com/api/v2_1/orders/")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header("Authorization", "Bearer: " + payuResponse.getAccess_token())
                        .post(payload);
        PayuResponse payuDetails = response.readEntity(PayuResponse.class);
        return payuDetails.getRedirectUri();
    }

    private PayuAuthResponse getAccessToken() {
        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.text("grant_type=client_credentials&#38;" +
                "client_id=" + clientId + ";" +
                "client_secret=" + secret);
        Response response = client.target("https://secure.snd.payu.com/pl/standard/user/oauth/authorize")
                        .request(MediaType.TEXT_PLAIN_TYPE)
                        .post(payload);
        return response.readEntity(PayuAuthResponse.class);
    }

    private String mapPayloadToString(Payload data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
