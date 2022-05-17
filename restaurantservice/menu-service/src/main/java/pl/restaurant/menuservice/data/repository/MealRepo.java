package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.MealEntity;

public interface MealRepo extends JpaRepository<MealEntity, Integer> {
}
