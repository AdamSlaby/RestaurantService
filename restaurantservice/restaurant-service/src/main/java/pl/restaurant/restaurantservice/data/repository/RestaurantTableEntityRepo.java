package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableId;

import java.util.List;

public interface RestaurantTableEntityRepo extends JpaRepository<RestaurantTableEntity, RestaurantTableId> {
    @Query("select new pl.restaurant.restaurantservice.data.entity." +
            "FoodTableEntity(r.table.tableId, r.table.seatsNr) " +
            "from RestaurantTableEntity r where r.restaurant.restaurantId = :id " +
            "order by r.table.seatsNr asc")
    List<FoodTableEntity> getAllByRestaurantId(@Param("id") Long restaurantId);
}
