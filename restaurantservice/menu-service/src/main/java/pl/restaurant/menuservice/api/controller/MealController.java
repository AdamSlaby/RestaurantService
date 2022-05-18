package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.response.MealInfo;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.business.service.MealService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/meal")
@AllArgsConstructor
public class MealController {
    private MealService mealService;

    @GetMapping("/info/{id}")
    public MealInfo getMealInfo(@PathVariable("id") Integer mealId) {
        return mealService.getMealInfo(mealId);
    }

    @PostMapping("/list")
    public MealListView getMeals(@RequestBody @Valid MealFilters filters) {
        return mealService.getMeals(filters);
    }

    @PostMapping("/")
    public void addMeal(@ModelAttribute @Valid Meal meal) {
        mealService.addMeal(meal);
    }

    @PatchMapping("/{id}")
    public void updateMeal(@ModelAttribute @Valid Meal meal, @PathVariable("id") Integer mealId) {
        mealService.updateMeal(mealId, meal);
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable("id") Integer mealId) {
        mealService.deleteMeal(mealId);
    }
}
