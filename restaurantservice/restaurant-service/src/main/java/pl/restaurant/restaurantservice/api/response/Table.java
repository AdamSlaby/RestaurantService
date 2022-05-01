package pl.restaurant.restaurantservice.api.response;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private Long id;

    @NotNull(message = "Liczba miejsc przy stole jest wymagana")
    private int seatsNr;
}
