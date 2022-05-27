package pl.restaurant.orderservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestaurantOrderRepo extends JpaRepository<RestaurantOrderEntity, Long> {
    @Query("select new pl.restaurant.orderservice.api.response." +
            "OrderShortInfo(o.orderId, 'Online', o.price, o.orderDate, (o.deliveryDate is not null)) " +
            "from RestaurantOrderEntity o " +
            "where (:rId is null or o.restaurantId = :rId) and " +
            "(:oId is null or o.orderId = :oId) and " +
            "(:from is null or (o.orderDate > :from and o.orderDate < :to)) and " +
            "(:done is null or (o.deliveryDate is not null) = :done)")
    Page<OrderShortInfo> getOrders(@Param("rId") Long restaurantId,
                                   @Param("oId") Long orderId,
                                   @Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to,
                                   @Param("done") Boolean isCompleted,
                                   Pageable pageable);

    @EntityGraph(attributePaths = {"meals"})
    Optional<RestaurantOrderEntity> getByOrderId(Long orderId);

    @Query("select o " +
            "from RestaurantOrderEntity o " +
            "where o.deliveryDate is null and o.restaurantId = :rId")
    List<RestaurantOrderEntity> getActiveOrders(Long restaurantId);
}
