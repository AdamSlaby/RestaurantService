package pl.restaurant.restaurantservice.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @NotBlank(message = "Nazwa miasta jest wymagana")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{3,57}$", message = "Nazwa miasta jest nieprawidłowa")
    private String city;

    @NotBlank(message = "Nazwa ulicy jest wymagana")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż -.]{3,57}$", message = "Nazwa ulicy jest nieprawidłowa")
    private String street;

    @NotBlank(message = "Numer domu jest wymagany")
    @Pattern(regexp = "^([1-9]\\d*(?:[ -]?(?:[a-zA-Z]+|[0-9]\\d*))?)$", message = "Numer domu jest nieprawidłowy")
    @Size(max = 5, message = "Numer domu jest zbyt długi")
    private String houseNr;

    @NotBlank(message = "Numer mieszkania jest wymagany")
    @Pattern(regexp = "^[1-9]\\d{1,3}$", message = "Numer mieszkania jest nieprawidłowy")
    private String flatNr;

    @NotBlank(message = "Kod pocztowy jest wymagany")
    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Kod pocztowy jest nieprawidłowy")
    private String postcode;
}
