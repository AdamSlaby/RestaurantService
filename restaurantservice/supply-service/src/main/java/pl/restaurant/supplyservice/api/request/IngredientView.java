package pl.restaurant.supplyservice.api.request;

import lombok.*;
import pl.restaurant.supplyservice.api.response.Unit;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientView {
    @NotNull(message = "Identyfikator składnika nie może być pusty")
    @Min(value = 0, message = "Identyfikator składnika nie może być liczbą ujemną")
    private Integer id;

    private String name;

    @NotNull(message = "Ilość składnika nie może być pusta")
    @Min(value = 0, message = "Ilość składnika  nie może być liczbą ujemną")
    @Digits(integer = 6, fraction = 2, message = "Ilość składnika jest nieprawidłowa")
    private BigDecimal amount;

    @Valid
    private Unit unit;
}
