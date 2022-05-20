package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.data.entity.MenuEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MenuMapper {
    public MenuView mapDataToView(MenuEntity menu) {
        HashMap<String, List<MealShortView>> mealMap = new HashMap<>();
        menu.getMeals()
                .forEach(el -> {
                    mealMap.computeIfAbsent(el.getType().getName(), k -> new ArrayList<>());
                    mealMap.get(el.getType().getName()).add(new MealShortView(el.getMealId(), el.getName()));
                });
        return new MenuView().builder()
                .id(menu.getMenuId())
                .season(menu.getSeason())
                .mealMap(mealMap)
                .build();
    }
}
