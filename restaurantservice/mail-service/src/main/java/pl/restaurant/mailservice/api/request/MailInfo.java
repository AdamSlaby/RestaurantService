package pl.restaurant.mailservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailInfo {
    @NotBlank(message = "Podane imię nie może być puste")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$", message = "Podane imię jest nieprawidłowe")
    private String name;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Podany email jest nieprawidłowy")
    @Size(max = 30, message = "Podany email jest za długi")
    private String email;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$",
            message = "Numer telefonu jest nieprawidłowy")
    private String phoneNumber;

    @NotBlank(message = "Temat maila nie może być pusty")
    private String subject;

    @NotBlank(message = "Treść maila nie może być pusta")
    private String content;
}
