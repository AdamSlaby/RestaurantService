package pl.restaurant.menuservice.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MealIngredientId implements Serializable {
    @Column(name = "meal_id")
    private Integer mealId;

    @Column(name = "ingredient_id")
    private Integer ingredientId;
}
