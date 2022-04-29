package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.restaurantservice.data.entity.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
}
