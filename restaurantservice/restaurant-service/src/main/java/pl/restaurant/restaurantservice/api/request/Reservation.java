package pl.restaurant.restaurantservice.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 1, message = "Identyfikator restauracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotEmpty(message = "Imię osoby rezerwującej nie może być puste")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$", message = "Podane imię jest nieprawidłowe")
    private String name;

    @NotBlank(message = "Nazwisko pracownika jest wymagane")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}(-[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32})?$",
            message = "Podane nazwisko jest nieprawidłowe")
    private String surname;

    @NotBlank(message = "Numer telefonu pracownika jest wymagany")
    @Pattern(regexp = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$",
            message = "Podany numer telefonu jest nieprawidłowy")
    private String phoneNr;

    @NotBlank(message = "Email osoby rezerwującej jest wymagany")
    @Email(message = "Podany email jest nieprawidłowy")
    @Size(max = 30, message = "Podany email jest za długi")
    private String email;

    @NotNull(message = "Data i godzina rezerwacji nie może być pusta")
    @Future(message = "Data i godzina rezerwacji muszą być z przyszłości")
    private LocalDateTime fromHour;

    @NotNull(message = "Liczba osób w rezerwacji nie moze być pusta")
    private Integer peopleNr;

    @Valid
    private List<@NotNull(message = "Identyfikator stołu nie może być pusty") Long> tableIds;
}
