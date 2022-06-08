package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.mapper.MealMapper;
import pl.restaurant.menuservice.api.mapper.MenuMapper;
import pl.restaurant.menuservice.api.request.MealMenu;
import pl.restaurant.menuservice.api.response.*;
import pl.restaurant.menuservice.business.exception.meal.MealAlreadyAddedToMenuException;
import pl.restaurant.menuservice.business.exception.meal.MealNotFoundException;
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
    private MealRepo mealRepo;

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
    public List<DishListView> getAllDishesFromMenu() {
        String season = SEASONS[LocalDate.now().getMonth().getValue() - 1];
        MenuEntity menu = menuRepo.getBySeason(season)
                .orElseThrow(MenuNotFoundException::new);
        return menu.getMeals().stream()
                .map(MealMapper::mapDataToListView)
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
        List<MenuEntity> menus = menuRepo.findAllBy();
        return menus.stream()
                .map(MenuMapper::mapDataToView)
                .collect(Collectors.toList());
    }

    @Override
    public MealShortView addMealToMenu(MealMenu menuMeal) {
        MenuEntity menu = menuRepo.getByMenuId(menuMeal.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        MealEntity meal;
        if (NumberUtils.isParsable(menuMeal.getMeal())) {
            int mealId = Integer.parseInt(menuMeal.getMeal());
            meal = mealRepo.findById(mealId)
                    .orElseThrow(MealNotFoundException::new);
        } else
            meal = mealRepo.findByName(menuMeal.getMeal())
                    .orElseThrow(MealNotFoundException::new);
        if (menu.getMeals().contains(meal))
            throw new MealAlreadyAddedToMenuException();
        menu.getMeals().add(meal);
        menuRepo.save(menu);
        return MealMapper.mapDataToView(meal);
    }

    @Override
    public void removeMealFromMenu(Integer menuId, Integer mealId) {
        MenuEntity menu = menuRepo.getByMenuId(menuId)
                .orElseThrow(MenuNotFoundException::new);
        MealEntity meal = mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
        menu.getMeals().remove(meal);
        menuRepo.save(menu);
    }
}
