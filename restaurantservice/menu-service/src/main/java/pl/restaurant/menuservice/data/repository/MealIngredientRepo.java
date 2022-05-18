package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.MealIngredientEntity;
import pl.restaurant.menuservice.data.entity.MealIngredientId;

public interface MealIngredientRepo extends JpaRepository<MealIngredientEntity, MealIngredientId> {
    void deleteByMeal(MealEntity meal);
}
