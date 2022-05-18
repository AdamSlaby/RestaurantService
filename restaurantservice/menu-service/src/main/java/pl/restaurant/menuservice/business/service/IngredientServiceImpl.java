package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.data.repository.IngredientRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepo ingredientRepo;
    @Override
    public List<IngredientInfo> getAllIngredients() {
        return ingredientRepo.getAllIngredients();
    }
}
