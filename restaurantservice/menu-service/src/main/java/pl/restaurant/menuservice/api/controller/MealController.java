package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.request.Order;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.DishListView;
import pl.restaurant.menuservice.api.response.MealInfo;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.business.service.MealService;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/all")
    public List<DishListView> getAllMealsFromMenu() {
        return mealService.getAllMealsFromMenu();
    }

    @PostMapping("/list")
    public MealListView getMeals(@RequestBody @Valid MealFilters filters) {
        return mealService.getMeals(filters);
    }

    @GetMapping("/best")
    public List<Dish> getBestMeals() {
        return mealService.getBestMeals();
    }

    @PostMapping("/rollback/{id}")
    public void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                                      @RequestBody @Valid List<Order> orders) {
        mealService.rollbackOrderSupplies(restaurantId, orders);
    }

    @PostMapping("/valid/{id}")
    public void validateOrder(@PathVariable("id") Long restaurantId, @RequestBody @Valid Order order) {
        mealService.validateOrder(restaurantId, order);
    }

    @PostMapping("/meals/{id}")
    public String validateOrders(@PathVariable("id") Long restaurantId, @RequestBody @Valid List<Order> orders) {
        return mealService.validateOrders(restaurantId, orders);
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
