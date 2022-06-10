package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.data.entity.OrderMealId;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantOrderMealRepo extends JpaRepository<RestaurantOrderMealEntity, OrderMealId> {
    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(o.mealName , sum(o.quantity)) " +
            "from RestaurantOrderMealEntity o where o.order.orderDate > :from and " +
            "o.order.orderDate <= :to and o.order.deliveryDate is not null and " +
            "(:rId is null or o.order.restaurantId = :rId) " +
            "group by o.mealName")
    List<ChartData> getDishAmountChart(@Param("rId") Long restaurantId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(o.mealName , sum(o.price)) " +
            "from RestaurantOrderMealEntity o where o.order.orderDate > :from and " +
            "o.order.orderDate <= :to and o.order.deliveryDate is not null and " +
            "(:rId is null or o.order.restaurantId = :rId) " +
            "group by o.mealName")
    List<ChartData> getDishIncomeChart(@Param("rId") Long restaurantId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);
}
