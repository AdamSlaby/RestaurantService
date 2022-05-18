package pl.restaurant.menuservice.business.service;


import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.response.MealListView;

public interface MealService {
    MealListView getMeals(MealFilters filters);
}
