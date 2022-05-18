package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.data.entity.*;

import java.math.BigDecimal;

@UtilityClass
public class MealIngredientMapper {
    public static MealIngredientEntity mapToData(MealEntity meal, IngredientEntity ingredient,
                                                 UnitEntity unit, BigDecimal quantity) {
        return new MealIngredientEntity().builder()
                .mealIngredientId(new MealIngredientId(meal.getMealId(), ingredient.getIngredientId()))
                .meal(meal)
                .ingredient(ingredient)
                .quantity(quantity)
                .unit(unit)
                .build();
    }
}
