package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.MealIngredientEntity;
import pl.restaurant.menuservice.data.entity.MealIngredientId;

import javax.persistence.PreRemove;

public interface MealIngredientRepo extends JpaRepository<MealIngredientEntity, MealIngredientId> {
    @Modifying
    @Query("delete from MealIngredientEntity m where m.meal = :meal")
    void deleteAllByMeal(@Param("meal") MealEntity meal);
}
