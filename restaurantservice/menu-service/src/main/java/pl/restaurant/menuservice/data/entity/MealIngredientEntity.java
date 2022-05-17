package pl.restaurant.menuservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Meal_Ingredient")
public class MealIngredientEntity implements Serializable {
    @EmbeddedId
    private MealIngredientId mealIngredientId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("mealId")
    @JoinColumn(name = "meal_id")
    private MealEntity meal;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitEntity unit;
}
