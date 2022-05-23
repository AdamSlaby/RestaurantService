package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.restaurant.menuservice.api.mapper.MealIngredientMapper;
import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientNotFoundException;
import pl.restaurant.menuservice.data.entity.IngredientEntity;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.UnitEntity;
import pl.restaurant.menuservice.data.repository.IngredientRepo;
import pl.restaurant.menuservice.data.repository.MealIngredientRepo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepo ingredientRepo;
    private UnitService unitService;
    private MealIngredientRepo mealIngredientRepo;

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

    @Override
    public Integer getIngredient(String ingredient) {
        Optional<Integer> ingredientId = ingredientRepo.getIngredientByName(ingredient);
        return ingredientId.orElseGet(() -> ingredientRepo.save(new IngredientEntity(ingredient)).getIngredientId());
    }

    @Validated
    @Override
    public void addIngredientsForMeal(@Valid List<Ingredient> ingredients, MealEntity mealEntity, int i) {
        IngredientEntity ingredientEntity;
        Ingredient ingredient = ingredients.get(i);
        if (ingredient.getId() == -1)
            ingredientEntity = addIngredient(ingredient, i);
        else
            ingredientEntity = getIngredient(ingredient.getId(), i);
        UnitEntity unit = unitService.getUnit(ingredient.getUnitId(), i);
        mealIngredientRepo.save(MealIngredientMapper.mapToData(mealEntity, ingredientEntity, unit,
                ingredient.getAmount()));
    }

    @Override
    public boolean isIngredientsExists(List<Integer> ingredientIds) {
        return ingredientRepo.existsAllByIngredientIdIn(ingredientIds);
    }

    @Override
    public Map<Integer, String> getAllIngredientsMap() {
        return ingredientRepo.getAllIngredients().stream()
                .collect(Collectors.toMap(IngredientInfo::getId, IngredientInfo::getName));
    }
}
