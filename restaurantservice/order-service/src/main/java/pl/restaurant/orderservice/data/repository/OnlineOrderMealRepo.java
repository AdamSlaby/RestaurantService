package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;
import pl.restaurant.orderservice.data.entity.OrderMealId;

public interface OnlineOrderMealRepo extends JpaRepository<OnlineOrderMealEntity, OrderMealId> {
    @Modifying
    @Query("delete from OnlineOrderMealEntity o where o.id.orderId = :id")
    void deleteMealsByOrderId(@Param("id") Long orderId);
}
