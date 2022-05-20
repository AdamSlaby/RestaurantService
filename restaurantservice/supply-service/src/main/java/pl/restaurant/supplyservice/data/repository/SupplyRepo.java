package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;

public interface SupplyRepo extends JpaRepository<SupplyEntity, RestaurantIngredientId> {
}
