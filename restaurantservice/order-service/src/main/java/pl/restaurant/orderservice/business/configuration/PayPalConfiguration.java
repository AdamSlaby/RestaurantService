package pl.restaurant.orderservice.business.configuration;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PayPalConfiguration {
    @Value("${paypal.mode}")
    private String mode;

    @Value("${paypal.client.app}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String secretId;

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, secretId, mode);
    }
}
