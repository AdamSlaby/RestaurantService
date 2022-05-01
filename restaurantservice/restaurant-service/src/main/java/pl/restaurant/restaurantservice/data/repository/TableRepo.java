package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;

import java.util.List;

public interface TableRepo extends JpaRepository<FoodTableEntity, Long> {
    List<FoodTableEntity> getAllBySeatsNr(int seatsNr);
}
