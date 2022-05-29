package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.request.MealMenu;
import pl.restaurant.menuservice.api.response.*;

import java.util.List;

public interface MenuService {
    List<Dish> getDishesFromMenu();
    List<DishListView> getAllDishesFromMenu();
    List<DishOrderView> getDishOrderViewsFromMenu();
    List<MenuView> getAllMenus();
    MealShortView addMealToMenu(MealMenu mealMenu);
    void removeMealFromMenu(Integer menuId, Integer mealId);
}
