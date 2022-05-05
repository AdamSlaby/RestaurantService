package pl.restaurant.employeeservice.business.service;

import pl.restaurant.employeeservice.api.request.Credentials;
import pl.restaurant.employeeservice.api.response.KeycloakResponse;

public interface LoginService {
    KeycloakResponse login(Credentials credentials);
}
