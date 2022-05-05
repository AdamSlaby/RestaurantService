package pl.restaurant.employeeservice.business.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.employeeservice.api.request.Credentials;
import pl.restaurant.employeeservice.api.response.KeycloakResponse;

@Service
public class LoginServiceImpl implements LoginService {
    @Value("${app.keycloak.login.url}")
    private String loginUrl;
    @Value("${app.keycloak.client-secret}")
    private String clientSecret;
    @Value("${app.keycloak.grant-type}")
    private String grantType;
    @Value("${app.keycloak.client-id}")
    private String clientId;

    private RestTemplate restTemplate;

    public LoginServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public KeycloakResponse login(Credentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", credentials.getUsername());
        map.add("password", credentials.getPassword());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<KeycloakResponse> loginResponse = restTemplate
                .postForEntity(loginUrl, httpEntity, KeycloakResponse.class);
        return loginResponse.getBody();
    }


}
