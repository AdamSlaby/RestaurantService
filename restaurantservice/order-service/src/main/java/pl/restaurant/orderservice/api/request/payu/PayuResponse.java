package pl.restaurant.orderservice.api.request.payu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayuResponse {
    private Status status;
    private String redirectUri;
    private String orderId;
}
