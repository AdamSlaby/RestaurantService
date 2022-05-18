package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.request.MenuMeal;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.DishOrderView;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;

import java.util.List;

public interface MenuService {
    List<Dish> getDishesFromMenu();
    List<DishOrderView> getDishOrderViewsFromMenu();
    List<MenuView> getAllMenus();
    MealShortView addMealToMenu(MenuMeal menuMeal);
    void deleteMealFromMenu(Integer menuId, Integer mealId);
}
