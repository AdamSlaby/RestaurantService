package pl.restaurant.employeeservice.api.request;

import javax.validation.constraints.NotBlank;

public class Credentials {
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private String username;

    @NotBlank(message = "Hasło użytkownika nie może być puste")
    private String password;
}
