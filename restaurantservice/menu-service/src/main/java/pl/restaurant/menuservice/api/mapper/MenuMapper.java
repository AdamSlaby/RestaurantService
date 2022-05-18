package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.data.entity.MenuEntity;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MenuMapper {
    public MenuView mapDataToView(MenuEntity menu) {
        HashMap<String, List<MealShortView>> mealMap = new HashMap<>();
        mealMap.put(menu.getSeason(),
                menu.getMeals().stream()
                .map(el -> new MealShortView(el.getMealId(), el.getName()))
                .collect(Collectors.toList()));
        return new MenuView().builder()
                .id(menu.getMenuId())
                .season(menu.getSeason())
                .mealMap(mealMap)
                .build();
    }
}
