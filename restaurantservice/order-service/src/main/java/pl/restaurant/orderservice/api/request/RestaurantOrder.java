package pl.restaurant.orderservice.api.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrder {
    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restaracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotNull(message = "Numer stolika nie może być pusty")
    @Min(value = 1, message = "Numer stolika musi być liczbą dodatnią")
    private Long tableId;

    @Valid
    private List<Order> orders;

    @NotNull(message = "Cena zamówienia nie może być pusta")
    @Digits(integer = 5, fraction = 2, message = "Cena zamówienia jest nieprawidłowa")
    @Min(value = 0, message = "Cena zamówienia nie może być liczbą ujemną")
    private BigDecimal price;
}
