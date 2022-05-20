package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.business.service.IngredientService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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

    @PostMapping("/new")
    public Integer addIngredient(@RequestBody @NotEmpty(message = "Nazwa składnika nie może być pusta")
                                     @Size(max = 30, message = "Nazwa składnika jest za długa")
                                     String ingredient) {
        return ingredientService.addIngredient(ingredient);
    }
}
