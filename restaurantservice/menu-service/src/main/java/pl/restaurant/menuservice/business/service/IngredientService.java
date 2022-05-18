package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.data.entity.IngredientEntity;

import java.util.List;

public interface IngredientService {
    IngredientEntity getIngredient(Integer ingredientId, int index);
    List<IngredientInfo> getAllIngredients();

    IngredientEntity addIngredient(Ingredient ingredient, int index);
}
