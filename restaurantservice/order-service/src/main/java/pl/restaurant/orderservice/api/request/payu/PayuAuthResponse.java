package pl.restaurant.orderservice.api.request.payu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayuAuthResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Long expires_in;
    private String grant_type;
}
