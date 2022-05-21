package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;

import java.util.Optional;

public interface SupplyRepo extends JpaRepository<SupplyEntity, RestaurantIngredientId> {
    @EntityGraph(attributePaths = {"unit"})
    Optional<SupplyEntity> getByRestaurantIngredientId(RestaurantIngredientId id);
}
