package pl.restaurant.employeeservice.business.service;

import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
public class KeycloakService {
    private Keycloak keycloak = null;
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${app.keycloak.client-secret}")
    private String clientSecret;
    @Value("${app.keycloak.client-id}")
    private String clientId;
    @Value("${app.keycloak.username}")
    private String username;
    @Value("${app.keycloak.password}")
    private String password;


    public Keycloak getInstance(){
        if(keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloak;
    }

    public List<RoleRepresentation> getRoles(String[] roleNames, String clientId) {
        List<RoleRepresentation> roles = new LinkedList<>();

        for (String roleName : roleNames) {
            roles.add(getInstance()
                    .realm(realm)
                    .clients()
                    .get(clientId)
                    .roles()
                    .get(roleName)
                    .toRepresentation());
        }
        return roles;
    }

    public UserResource getUser(String username) {
        String userId = getInstance()
                .realm(realm)
                .users()
                .search(username)
                .get(0)
                .getId();
        return getInstance()
                .realm(getRealm())
                .users()
                .get(userId);
    }

    public ClientRepresentation getClientRep() {
        return getInstance()
                .realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);
    }

    public String getRealm() {
        return realm;
    }

    public String getClientId() {
        return clientId;
    }
}
