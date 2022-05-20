package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.response.*;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.TypeEntity;

import java.util.stream.Collectors;

@UtilityClass
public class MealMapper {
    public static MealInfo mapDataToInfo(MealEntity mealEntity) {
        return new MealInfo().builder()
                .id(mealEntity.getMealId())
                .name(mealEntity.getName())
                .typeId(mealEntity.getType().getTypeId())
                .price(mealEntity.getPrice())
                .imageUrl(mealEntity.getImageUrl())
                .ingredients(mealEntity.getIngredients().stream()
                        .map(el -> new IngredientView(el.getIngredient().getIngredientId(),
                                el.getIngredient().getName(), el.getQuantity(),
                                new Unit(el.getUnit().getUnitId(), el.getUnit().getName())))
                        .collect(Collectors.toList()))
                .build();
    }

    public static MealEntity mapObjectToData(Meal meal, TypeEntity type, String imageUrl) {
        return new MealEntity().builder()
                .name(meal.getName())
                .price(meal.getPrice())
                .imageUrl(imageUrl)
                .isBest(false)
                .type(type)
                .build();
    }

    public static MealShortView mapDataToView(MealEntity meal) {
        return new MealShortView(meal.getMealId(), meal.getName());
    }

    public static Dish mapDataToDish(MealEntity meal) {
        return new Dish().builder()
                .id(meal.getMealId())
                .name(meal.getName())
                .type(meal.getType().getName())
                .ingredients(getIngredientsString(meal))
                .price(meal.getPrice())
                .isBest(meal.isBest())
                .build();
    }

    public static DishOrderView mapDataToOrderView(MealEntity meal) {
        return new DishOrderView().builder()
                .id(meal.getMealId())
                .imageUrl(meal.getImageUrl())
                .name(meal.getName())
                .type(meal.getType().getName())
                .ingredients(getIngredientsString(meal))
                .amount(1)
                .price(meal.getPrice())
                .build();
    }

    private static String getIngredientsString(MealEntity meal) {
        StringBuilder builder = new StringBuilder();
        meal.getIngredients().forEach(el ->
                builder.append(el.getIngredient().getName()).append(", "));
        if (meal.getIngredients().size() > 1) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 2);
        }
        return builder.toString();
    }
}
