package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.IngredientEntity;

public interface IngredientRepo extends JpaRepository<IngredientEntity, Integer> {
}
