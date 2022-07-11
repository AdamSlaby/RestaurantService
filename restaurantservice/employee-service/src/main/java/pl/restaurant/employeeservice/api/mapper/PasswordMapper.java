package pl.restaurant.employeeservice.api.mapper;

import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;

@UtilityClass
public class PasswordMapper {
    public static CredentialRepresentation mapPasswordToCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
