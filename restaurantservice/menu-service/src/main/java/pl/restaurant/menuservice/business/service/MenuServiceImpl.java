package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.mapper.MealMapper;
import pl.restaurant.menuservice.api.mapper.MenuMapper;
import pl.restaurant.menuservice.api.request.MenuMeal;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.DishOrderView;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.business.exception.menu.MenuNotFoundException;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.MenuEntity;
import pl.restaurant.menuservice.data.repository.MealRepo;
import pl.restaurant.menuservice.data.repository.MenuRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {
    private static final String SEASONS[] = {
            "Zima", "Zima", "Wiosna", "Wiosna", "Wiosna", "Lato",
            "Lato", "Lato", "Jesień", "Jesień", "Jesień", "Zima"
    };
    private MenuRepo menuRepo;
    private MealService mealService;

    @Override
    public List<Dish> getDishesFromMenu() {
        String season = SEASONS[LocalDate.now().getMonth().getValue() - 1];
        MenuEntity menu = menuRepo.getBySeason(season)
                .orElseThrow(MenuNotFoundException::new);
        return menu.getMeals().stream()
                .map(MealMapper::mapDataToDish)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishOrderView> getDishOrderViewsFromMenu() {
        String season = SEASONS[LocalDate.now().getMonth().getValue() - 1];
        MenuEntity menu = menuRepo.getBySeason(season)
                .orElseThrow(MenuNotFoundException::new);
        return menu.getMeals().stream()
                .map(MealMapper::mapDataToOrderView)
                .collect(Collectors.toList());
    }
    @Override
    public List<MenuView> getAllMenus() {
        List<MenuEntity> menus = menuRepo.findAll();
        return menus.stream()
                .map(MenuMapper::mapDataToView)
                .collect(Collectors.toList());
    }

    @Override
    public MealShortView addMealToMenu(MenuMeal menuMeal) {
        MenuEntity menu = menuRepo.findById(menuMeal.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        MealEntity meal;
        if (NumberUtils.isParsable(menuMeal.getMeal())) {
            int mealId = Integer.parseInt(menuMeal.getMeal());
            meal = mealService.getMeal(mealId);
        } else
            meal = mealService.getMeal(menuMeal.getMeal());
        menu.getMeals().add(meal);
        menuRepo.save(menu);
        return MealMapper.mapDataToView(meal);
    }

    @Override
    public void deleteMealFromMenu(Integer menuId, Integer mealId) {
        MenuEntity menu = menuRepo.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
        MealEntity meal = mealService.getMeal(mealId);
        menu.getMeals().remove(meal);
        menuRepo.save(menu);
    }
}
