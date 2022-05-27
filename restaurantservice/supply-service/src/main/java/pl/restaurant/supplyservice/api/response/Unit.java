package pl.restaurant.supplyservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
    @NotNull(message = "Identyfikator jednostki miary jest nieprawidłowy")
    @Min(value = 0, message = "Identyfikator jednostki miary nie może być liczbą ujemną")
    private Integer id;
    private String name;
}
