package pl.restaurant.orderservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.business.model.MealAmount;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OnlineOrderRepo extends JpaRepository<OnlineOrderEntity, Long> {
    @EntityGraph(attributePaths = {"meals", "address"})
    Optional<OnlineOrderEntity> getByOrderId(Long orderId);

    @Query("select new pl.restaurant.orderservice.api.response." +
            "OrderShortInfo(o.orderId, 'Online', o.price, o.orderDate, o.deliveryDate) " +
            "from OnlineOrderEntity o " +
            "where (:rId is null or o.restaurantId = :rId) and " +
            "(:oId is null or o.orderId = :oId) and " +
            "(:from is null or (o.orderDate > :from and o.orderDate < :to)) and " +
            "(:done is null or o.deliveryDate is not null)")
    Page<OrderShortInfo> getDeliveredOrders(@Param("rId") Long restaurantId,
                                            @Param("oId") Long orderId,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to,
                                            @Param("done") Boolean isCompleted,
                                            Pageable pageable);

    @Query("select new pl.restaurant.orderservice.api.response." +
            "OrderShortInfo(o.orderId, 'Online', o.price, o.orderDate, o.deliveryDate) " +
            "from OnlineOrderEntity o " +
            "where (:rId is null or o.restaurantId = :rId) and " +
            "(:oId is null or o.orderId = :oId) and " +
            "(:from is null or (o.orderDate > :from and o.orderDate < :to)) and " +
            "(:done is null or o.deliveryDate is null)")
    Page<OrderShortInfo> getNotDeliveredOrders(@Param("rId") Long restaurantId,
                                            @Param("oId") Long orderId,
                                            @Param("from")LocalDateTime from,
                                            @Param("to") LocalDateTime to,
                                            @Param("done") Boolean isCompleted,
                                            Pageable pageable);

    @Query("select distinct o " +
            "from OnlineOrderEntity o left join fetch o.meals " +
            "where o.deliveryDate is null and o.restaurantId = :rId and o.isPaid = true")
    List<OnlineOrderEntity> getActiveOrders(@Param("rId") Long restaurantId);

    @Query("select distinct o from OnlineOrderEntity o join fetch o.meals " +
            "where o.orderDate > :from and o.orderDate < :to")
    List<OnlineOrderEntity> getOrdersByDate(@Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to);

    @Query("select sum(o.price) from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and " +
            "(:rId is null or o.restaurantId = :rId)")
    BigDecimal getTodayIncome(@Param("rId") Long restaurantId,
                              @Param("from") LocalDateTime from,
                              @Param("to") LocalDateTime to);

    @Query("select count(o) from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId)")
    Integer getTodayDeliveredOrders(@Param("rId") Long restaurantId,
                                    @Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);

//    @Query("select count(o.meals.size) from OnlineOrderEntity o where o.orderDate > :from and " +
//            "o.orderDate <= :to and o.deliveryDate is not null and " +
//            "(:rId is null or o.restaurantId = :rId)")
//    Integer getTodayDeliveredMealsAmount(@Param("rId") Long restaurantId,
//                                         @Param("from") LocalDateTime from,
//                                         @Param("to") LocalDateTime to);

    @Query("select count(o) from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.deliveryDate is null and o.isPaid = true and " +
            "(:rId is null or o.restaurantId = :rId)")
    Integer getActiveOrdersAmount(@Param("rId") Long restaurantId,
                                  @Param("from") LocalDateTime from,
                                  @Param("to") LocalDateTime to);

    @Query("select o.orderDate from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and " +
            "(:rId is null or o.restaurantId = :rId)")
    List<LocalDateTime> getOrderAmountFromHours(@Param("rId") Long restaurantId,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);


    @Query("select count(o) from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId)")
    Long getCompareOrderChart(@Param("rId") Long restaurantId,
                              @Param("from") LocalDateTime from,
                              @Param("to") LocalDateTime to);

    @Query("select sum(o.price) from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId)")
    BigDecimal getCompareOrderIncomeChart(@Param("rId") Long restaurantId,
                                    @Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(concat(o.paymentMethod, '') , count(o)) " +
            "from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId) " +
            "group by o.paymentMethod")
    List<ChartData> getPaymentMethodAmountChart(@Param("rId") Long placeId,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.api.response.chart.ChartData(size(o.meals) , count(o.orderId)) " +
            "from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId) " +
            "group by size(o.meals)")
    List<ChartData> getOrdersAmountWithDishesAmountChart(@Param("rId") Long placeId,
                                                         @Param("from") LocalDateTime from,
                                                         @Param("to") LocalDateTime to);

    @Query("select new pl.restaurant.orderservice.api.response.chart" +
            ".ChartData(size(o.meals), avg(function('TIME_TO_SEC', function('TIMEDIFF', o.deliveryDate, o.orderDate)))) " +
            "from OnlineOrderEntity o where o.orderDate > :from and " +
            "o.orderDate <= :to and o.isPaid = true and o.deliveryDate is not null and " +
            "(:rId is null or o.restaurantId = :rId) " +
            "group by size(o.meals)")
    List<ChartData> getAvgCompletionTimeWithDishesAmountChart(@Param("rId") Long placeId,
                                                              @Param("from") LocalDateTime from,
                                                              @Param("to") LocalDateTime to);
}
