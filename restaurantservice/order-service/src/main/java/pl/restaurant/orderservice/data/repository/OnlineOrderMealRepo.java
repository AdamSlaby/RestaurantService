package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.OrderMealId;

public interface OnlineOrderMealRepo extends JpaRepository<OnlineOrderMealRepo, OrderMealId> {
}
