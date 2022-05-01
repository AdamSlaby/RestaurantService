package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.restaurantservice.data.entity.FoodTable;

public interface TableRepo extends JpaRepository<FoodTable, Long> {
}
