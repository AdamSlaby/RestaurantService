package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.api.response.IngredientView;
import pl.restaurant.menuservice.api.response.Unit;
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


    public static IngredientView mapDataToView(MealIngredientEntity ingredient) {
        return new IngredientView().builder()
                .id(ingredient.getIngredient().getIngredientId())
                .name(ingredient.getIngredient().getName())
                .amount(ingredient.getQuantity())
                .unit(new Unit(ingredient.getUnit().getUnitId(), ingredient.getUnit().getName()))
                .build();

    }
}
