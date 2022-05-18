package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientNotFoundException;
import pl.restaurant.menuservice.data.entity.IngredientEntity;
import pl.restaurant.menuservice.data.repository.IngredientRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepo ingredientRepo;

    @Override
    public IngredientEntity getIngredient(Integer ingredientId, int index) {
        return ingredientRepo.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException(index));
    }

    @Override
    public List<IngredientInfo> getAllIngredients() {
        return ingredientRepo.getAllIngredients();
    }

    @Override
    public IngredientEntity addIngredient(Ingredient ingredient, int index) {
        if (ingredientRepo.existsByName(ingredient.getName()))
            throw new IngredientAlreadyExistsException(index);
        return ingredientRepo.save(new IngredientEntity(ingredient.getName()));
    }
}
