package pl.restaurant.supplyservice.api.request;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidation {
    @NotNull(message = "Identyfikator posiłku nie może być pusty")
    @Min(value = 0, message = "Identyfikator posiłku nie może być liczbą ujemną")
    private Integer dishId;

    private String name;

    @NotNull(message = "Ilość posiłków nie może być pusta")
    @Min(value = 1, message = "Ilość posiłków musi być liczbą dodatnią")
    private Integer amount;

    @Valid
    private List<IngredientView> ingredients;
}
