package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidation {
    private Integer dishId;
    private String name;
    private Integer amount;
    private List<IngredientView> ingredients;
}
