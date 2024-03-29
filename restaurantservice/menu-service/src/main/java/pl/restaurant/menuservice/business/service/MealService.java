package pl.restaurant.menuservice.business.service;


import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.request.Order;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.DishListView;
import pl.restaurant.menuservice.api.response.MealInfo;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.data.entity.MealEntity;

import java.util.List;

public interface MealService {
    List<DishListView> getAllMealsFromMenu();
    MealListView getMeals(MealFilters filters);

    List<Dish> getBestMeals();

    MealInfo getMealInfo(Integer mealId);

    void addMeal(Meal meal);

    void updateMeal(Integer mealId, Meal meal);

    void deleteMeal(Integer mealId);

    String validateOrders(Long restaurantId, List<Order> orders, String authHeader);

    void validateOrder(Long restaurantId, Order order);

    void rollbackOrderSupplies(Long restaurantId, List<Order> orders, String authHeader);
}
