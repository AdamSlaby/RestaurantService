package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.data.entity.OrderMealId;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

import java.util.List;

public interface RestaurantOrderMealRepo extends JpaRepository<RestaurantOrderMealEntity, OrderMealId> {
}
