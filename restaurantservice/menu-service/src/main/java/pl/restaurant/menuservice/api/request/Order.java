package pl.restaurant.menuservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @NotNull(message = "Identyfikator posiłku nie może być pusty")
    @Min(value = 0, message = "Identyfikator posiłku nie może być liczbą ujemną")
    private Integer dishId;

    @NotNull(message = "Ilość posiłków nie może być pusta")
    @Min(value = 1, message = "Ilość posiłków musi być liczbą dodatnią")
    private Integer amount;

    @NotNull(message = "Cena za posiłki nie może być pusta")
    @Digits(integer = 3, fraction = 2, message = "Cena za posiłki jest nieprawidłowa")
    @Min(value = 0, message = "Cena za posiłki nie może być liczbą ujemną")
    private BigDecimal price;
}
