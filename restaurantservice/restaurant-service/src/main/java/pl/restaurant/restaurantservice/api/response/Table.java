package pl.restaurant.restaurantservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private Long id;

    @NotNull(message = "Liczba miejsc przy stole jest wymagana")
    private int seatsNr;
}
