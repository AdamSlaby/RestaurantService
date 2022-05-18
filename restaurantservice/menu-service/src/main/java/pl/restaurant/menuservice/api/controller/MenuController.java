package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.request.MealMenu;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.DishOrderView;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.business.service.MenuService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class MenuController {
    private MenuService menuService;

    @GetMapping("/dishes")
    public List<Dish> getDishesFromMenu() {
        return menuService.getDishesFromMenu();
    }

    @GetMapping("/dishes/list")
    public List<DishOrderView> getDishOrderViewsFromMenu() {
        return menuService.getDishOrderViewsFromMenu();
    }

    @GetMapping("/all")
    public List<MenuView> getAllMenus() {
        return menuService.getAllMenus();
    }

    @PostMapping("/")
    public MealShortView addMealToMenu(@RequestBody @Valid MealMenu mealMenu) {
        return menuService.addMealToMenu(mealMenu);
    }

    @DeleteMapping("/{id}")
    public void removeMealFromMenu(@PathVariable("id") Integer menuId,
                                   @RequestParam("meal") Integer mealId) {
        menuService.removeMealFromMenu(menuId, mealId);
    }
}
