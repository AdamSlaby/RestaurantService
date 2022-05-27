package pl.restaurant.orderservice.api.request;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @NotNull(message = "Identyfikator posiłku nie może być pusty")
    @Min(value = 0, message = "Identyfikator posiłku nie może być liczbą ujemną")
    private Integer dishId;

    @NotBlank(message = "Nazwa posiłku nie może być pusta")
    @Size(max = 30, message = "Nazwa posiłku jest za długa")
    private String name;

    @NotNull(message = "Ilość posiłków nie może być pusta")
    @Min(value = 1, message = "Ilość posiłków musi być liczbą dodatnią")
    private Integer amount;

    @NotNull(message = "Cena za posiłki nie może być pusta")
    @Digits(integer = 3, fraction = 2, message = "Cena za posiłki jest nieprawidłowa")
    @Min(value = 0, message = "Cena za posiłki nie może być liczbą ujemną")
    private BigDecimal price;
}
