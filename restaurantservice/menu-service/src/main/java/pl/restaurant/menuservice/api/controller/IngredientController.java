package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.business.service.IngredientService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ingredient")
@AllArgsConstructor
public class IngredientController {
    private IngredientService ingredientService;

    @GetMapping("/")
    public List<IngredientInfo> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/map")
    public Map<Integer, String> getAllIngredientsMap() {
        return ingredientService.getAllIngredientsMap();
    }

    @PostMapping("/name")
    public Integer getIngredient(@RequestBody @NotEmpty(message = "Nazwa składnika nie może być pusta")
                                     @Size(max = 30, message = "Nazwa składnika jest za długa")
                                     String ingredient) {
        return ingredientService.getIngredient(ingredient);
    }

    @PostMapping("/exists")
    public boolean isIngredientsExists(@RequestBody List<@NotNull Integer> ingredientIds) {
        return ingredientService.isIngredientsExists(ingredientIds);
    }
}
