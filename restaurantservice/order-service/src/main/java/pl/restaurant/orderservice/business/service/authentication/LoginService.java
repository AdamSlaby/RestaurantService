package pl.restaurant.orderservice.business.service.authentication;

import pl.restaurant.orderservice.api.request.KeycloakResponse;

public interface LoginService {
    KeycloakResponse login();
    void logout(String refreshToken);
}
