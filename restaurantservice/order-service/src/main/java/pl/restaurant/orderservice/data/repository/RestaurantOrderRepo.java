package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;

public interface RestaurantOrderRepo extends JpaRepository<RestaurantOrderEntity, Long> {
}
