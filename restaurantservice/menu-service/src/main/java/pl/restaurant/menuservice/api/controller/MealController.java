package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.business.service.MealService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/meal")
@AllArgsConstructor
public class MealController {
    private MealService mealService;

    @PostMapping("/")
    public MealListView getMeals(@RequestBody @Valid MealFilters filters) {
        return mealService.getMeals(filters);
    }
}
