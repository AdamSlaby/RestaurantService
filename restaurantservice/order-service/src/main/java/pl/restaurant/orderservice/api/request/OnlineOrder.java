package pl.restaurant.orderservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.restaurant.orderservice.data.entity.PaymentMethod;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnlineOrder {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restaracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotEmpty(message = "Imię osoby rezerwującej nie może być puste")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$", message = "Podane imię jest nieprawidłowe")
    private String name;

    @NotBlank(message = "Nazwisko pracownika jest wymagane")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}(-[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32})?$",
            message = "Podane nazwisko jest nieprawidłowe")
    private String surname;

    @NotBlank(message = "Email osoby rezerwującej jest wymagany")
    @Email(message = "Podany email jest nieprawidłowy")
    @Size(max = 30, message = "Podany email jest za długi")
    private String email;

    @NotBlank(message = "Numer telefonu pracownika jest wymagany")
    @Pattern(regexp = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$",
            message = "Podany numer telefonu jest nieprawidłowy")
    private String phoneNr;

    @Valid
    private Address address;

    private Integer floor;

    @NotNull(message = "Metoda płatności nie może być pusta")
    private PaymentMethod paymentMethod;

    @Valid
    private List<Order> orders;
}
