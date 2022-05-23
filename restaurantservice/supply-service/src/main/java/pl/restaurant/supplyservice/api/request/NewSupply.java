package pl.restaurant.supplyservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewSupply {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restauracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotBlank(message = "Nazwa składnika nie może być pusta")
    @Size(max = 30, message = "Nazwa składnika jest za długa")
    private String ingredientName;

    @NotNull(message = "Ilość składnika nie może być pusta")
    @Min(value = 0, message = "Ilość składnika  nie może być liczbą ujemną")
    @Digits(integer = 6, fraction = 2, message = "Ilość składnika jest nieprawidłowa")
    private BigDecimal quantity;

    @NotNull(message = "Identyfikator jednostki miary jest nieprawidłowy")
    @Min(value = 0, message = "Identyfikator jednostki miary nie może być liczbą ujemną")
    private Integer unitId;
}
