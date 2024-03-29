package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.business.model.MealAmount;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;
import pl.restaurant.orderservice.data.entity.OrderMealId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OnlineOrderMealRepo extends JpaRepository<OnlineOrderMealEntity, OrderMealId> {
    @Modifying
    @Query("delete from OnlineOrderMealEntity o where o.id.orderId = :id")
    void deleteMealsByOrderId(@Param("id") Long orderId);

    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(o.mealName , sum(o.quantity)) " +
            "from OnlineOrderMealEntity o where o.order.orderDate > :from and " +
            "o.order.orderDate <= :to and o.order.isPaid = true and o.order.deliveryDate is not null and " +
            "(:rId is null or o.order.restaurantId = :rId) " +
            "group by o.mealName")
    List<ChartData> getDishAmountChart(@Param("rId") Long restaurantId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(o.mealName , sum(o.price)) " +
            "from OnlineOrderMealEntity o where o.order.orderDate > :from and " +
            "o.order.orderDate <= :to and o.order.isPaid = true and o.order.deliveryDate is not null and " +
            "(:rId is null or o.order.restaurantId = :rId) " +
            "group by o.mealName")
    List<ChartData> getDishIncomeChart(@Param("rId") Long restaurantId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.business.model." +
            "MealAmount(o.id.mealId, sum(o.quantity)) " +
            "from OnlineOrderMealEntity o " +
            "where o.order.isPaid = true and " +
            "o.order.orderDate <= :to and o.order.orderDate >= :from " +
            "group by o.id.mealId")
    List<MealAmount> getMealsAmount(@Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);

    @Query("select sum(o.quantity) from OnlineOrderMealEntity o " +
            "where o.order.orderDate > :from and " +
            "o.order.orderDate <= :to and o.order.deliveryDate is not null and " +
            "(:rId is null or o.order.restaurantId = :rId)")
    Integer getTodayDeliveredMealsAmount(@Param("rId") Long restaurantId,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to);
}
