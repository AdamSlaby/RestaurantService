package pl.restaurant.supplyservice.api.request;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplyInfo {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restauracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotNull(message = "Identyfikator składnika nie może być pusty")
    @Min(value = 0, message = "Identyfikator składnika nie może być liczbą ujemną")
    private Integer ingredientId;

    @NotNull(message = "Ilość składnika nie może być pusta")
    @Min(value = 0, message = "Ilość składnika  nie może być liczbą ujemną")
    @Digits(integer = 6, fraction = 3, message = "Ilość składnika jest nieprawidłowa")
    private BigDecimal quantity;

    @NotNull(message = "Identyfikator jednostki miary jest nieprawidłowy")
    @Min(value = 0, message = "Identyfikator jednostki miary nie może być liczbą ujemną")
    private Integer unitId;
}
