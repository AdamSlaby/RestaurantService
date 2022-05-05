package pl.restaurant.employeeservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    private String username;

    @NotBlank(message = "Hasło użytkownika nie może być puste")
    private String password;
}
