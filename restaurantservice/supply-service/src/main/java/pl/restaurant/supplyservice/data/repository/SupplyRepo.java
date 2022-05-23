package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;

import java.util.List;
import java.util.Optional;

public interface SupplyRepo extends JpaRepository<SupplyEntity, RestaurantIngredientId> {
    @EntityGraph(attributePaths = {"unit"})
    Optional<SupplyEntity> getByRestaurantIngredientId(RestaurantIngredientId id);

    @Query("select new pl.restaurant.supplyservice.api.request." +
            "SupplyInfo(s.restaurantIngredientId.restaurantId, s.restaurantIngredientId.ingredientId, s.quantity, s.unit.unitId) " +
            "from SupplyEntity s " +
            "where s.restaurantIngredientId.restaurantId = :rId")
    List<SupplyInfo> getAllSupplies(@Param("rId") Long restaurantId);

}
