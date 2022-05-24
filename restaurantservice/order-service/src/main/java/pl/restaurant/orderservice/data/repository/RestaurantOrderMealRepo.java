package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.OrderMealId;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

public interface RestaurantOrderMealRepo extends JpaRepository<RestaurantOrderMealEntity, OrderMealId> {
}
