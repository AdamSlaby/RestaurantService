package pl.restaurant.menuservice.business.service;


import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.MealInfo;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.data.entity.MealEntity;

import java.util.List;

public interface MealService {
    MealEntity getMeal(String name);
    MealEntity getMeal(Integer mealId);
    MealListView getMeals(MealFilters filters);

    List<Dish> getBestMeals();

    MealInfo getMealInfo(Integer mealId);

    void addMeal(Meal meal);

    void updateMeal(Integer mealId, Meal meal);

    void deleteMeal(Integer mealId);
}
