package pl.restaurant.restaurantservice.api.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import pl.restaurant.restaurantservice.api.response.Table;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private Long restaurantId;

    @Valid
    private Address address;

    @NotBlank(message = "Email restauracji jest wymagany")
    @Email(message = "Podany email jest nieprawidłowy")
    @Size(max = 30, message = "Podany email jest za długi")
    private String email;

    @NotBlank(message = "Numer telefonu restauracji jest wymagany")
    @Pattern(regexp = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$",
            message = "Numer telefonu jest nieprawidłowy")
    private String phoneNr;

    @NotNull(message = "Opłata za dowóz jest wymagana")
    @Min(value = 0, message = "Opłata za dowóz nie może być liczbą ujemną")
    @Digits(integer = 2, fraction = 2, message = "Podana opłata za dwóz jest nieprawidłowa")
    private BigDecimal deliveryFee;

    @NotNull(message = "Minimalna cena dostawy jest wymagana")
    @Min(value = 0, message = "Minimalna cena dostawy nie może być liczbą ujemną")
    @Digits(integer = 3, fraction = 2, message = "Podana minimalna cena dostawy jest nieprawidłowa")
    private BigDecimal minimalDeliveryPrice;

    @Valid
    @NotEmpty(message = "Lista godzin otwarcia nie może być pusta")
    @Size(min = 7, max = 7, message = "Lista godzin otwarcia musi zawierać wszystkie 7 dni")
    private List<OpeningHour> openingHours;

    @Valid
    @NotEmpty(message = "Lista stolików w restauracji nie może być pusta")
    private List<Table> tables;
}
