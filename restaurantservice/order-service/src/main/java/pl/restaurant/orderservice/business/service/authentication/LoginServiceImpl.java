package pl.restaurant.orderservice.business.service.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.orderservice.api.request.KeycloakResponse;

@Service
public class LoginServiceImpl implements LoginService {
    @Value("${app.keycloak.login.url}")
    private String loginUrl;

    @Value("${app.keycloak.logout.url}")
    private String logoutUrl;
    @Value("${app.keycloak.client-secret}")
    private String clientSecret;
    @Value("${app.keycloak.grant-type}")
    private String grantType;
    @Value("${app.keycloak.client-id}")
    private String clientId;
    @Value("${app.keycloak.username}")
    private String username;
    @Value("${app.keycloak.password}")
    private String password;

    private final RestTemplate restTemplate;

    public LoginServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public KeycloakResponse login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        try {
            ResponseEntity<KeycloakResponse> loginResponse = restTemplate
                    .postForEntity(loginUrl, httpEntity, KeycloakResponse.class);
            return loginResponse.getBody();
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong. Please check connection to keycloak");
        }
    }

    @Override
    public void logout(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        try {
            restTemplate.postForEntity(logoutUrl, httpEntity, Object.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
