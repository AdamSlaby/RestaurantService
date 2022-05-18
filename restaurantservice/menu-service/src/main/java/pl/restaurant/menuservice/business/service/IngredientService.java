package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.response.IngredientInfo;

import java.util.List;

public interface IngredientService {
    List<IngredientInfo> getAllIngredients();
}
