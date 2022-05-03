package pl.restaurant.employeeservice.api.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restauracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotBlank(message = "Imię pracownika jest wymagane")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}$", message = "Podane imię jest nieprawidłowe")
    private String name;

    @NotBlank(message = "Nazwisko pracownika jest wymagane")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32}(-[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,32})?$",
            message = "Podane nazwisko jest nieprawidłowe")
    private String surname;

    @NotNull(message = "Identyfikator stanowiska pracy nie może być pusty")
    @Min(value = 0, message = "Identyfikator stanowsika pracy nie może być liczbą ujemną")
    private Integer workstationId;

    @NotBlank(message = "Numer telefonu pracownika jest wymagany")
    @Pattern(regexp = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$",
            message = "Podany numer telefonu jest nieprawidłowy")
    private String phoneNr;

    @NotBlank(message = "Pesel pracownika jest wymagany")
    @Pattern(regexp = "^\\d{11}$", message = "Podany pesel jest nieprawidłowy")
    private String pesel;

    @NotBlank(message = "Numer konta bankowego pracownika jest wymagany")
    @Pattern(regexp = "^\\d{2}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}|\\d{26}$",
            message = "Podany numer konta bankowego jest nieprawidłowy")
    private String accountNr;

    @NotNull(message = "Pensja pracownika jest wymagana")
    @Digits(integer = 5, fraction = 2, message = "Podana pensja jest nieprawidłowa")
    private BigDecimal salary;

    @NotNull(message = "Status pracownika jest wymagany")
    private Boolean active;

    @NotNull(message = "Data zatrudnienia pracownika jest wymagana")
    private LocalDate employmentDate;

    private LocalDate dismissalDate;

    @Valid
    private Address address;
}
