package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.business.service.IngredientService;

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
}
