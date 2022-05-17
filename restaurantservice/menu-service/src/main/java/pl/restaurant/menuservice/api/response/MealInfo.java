package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealInfo {
    private Integer id;
    private String name;
    private Integer typeId;
    private BigDecimal price;
    private String imageUrl;
    private List<IngredientView> ingredients;
}
