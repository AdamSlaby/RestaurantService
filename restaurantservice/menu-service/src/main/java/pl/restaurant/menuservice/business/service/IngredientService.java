package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.data.entity.IngredientEntity;
import pl.restaurant.menuservice.data.entity.MealEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface IngredientService {
    IngredientEntity getIngredient(Integer ingredientId, int index);
    List<IngredientInfo> getAllIngredients();

    IngredientEntity addIngredient(Ingredient ingredient, int index);

    Integer getIngredient(String ingredient);

    void addIngredientsForMeal(@Valid List<Ingredient> ingredients, MealEntity mealEntity, int i);

    boolean isIngredientsExists(List<Integer> ingredientIds);

    Map<Integer, String> getAllIngredientsMap();
}
