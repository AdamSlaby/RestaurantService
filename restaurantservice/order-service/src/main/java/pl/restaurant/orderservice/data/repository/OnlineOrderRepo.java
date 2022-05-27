package pl.restaurant.orderservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OnlineOrderRepo extends JpaRepository<OnlineOrderEntity, Long> {
    @EntityGraph(attributePaths = {"meals", "address"})
    Optional<OnlineOrderEntity> getByOrderId(Long orderId);

    @Query("select new pl.restaurant.orderservice.api.response." +
            "OrderShortInfo(o.orderId, 'Online', o.price, o.orderDate, (o.deliveryDate is not null)) " +
            "from OnlineOrderEntity o " +
            "where (:rId is null or o.restaurantId = :rId) and " +
            "(:oId is null or o.orderId = :oId) and " +
            "(:from is null or (o.orderDate > :from and o.orderDate < :to)) and " +
            "(:done is null or (o.deliveryDate is not null) = :done)")
    Page<OrderShortInfo> getOrders(@Param("rId") Long restaurantId,
                                   @Param("oId") Long orderId,
                                   @Param("from")LocalDateTime from,
                                   @Param("to") LocalDateTime to,
                                   @Param("done") boolean isCompleted,
                                   Pageable pageable);

    @Query("select o " +
            "from OnlineOrderEntity o join fetch o.meals" +
            "where o.deliveryDate is null and o.restaurantId = :rId and o.isPaid = true")
    List<OnlineOrderEntity> getActiveOrders(@Param("rId") Long restaurantId);

    @Query("select o from OnlineOrderEntity o join fetch o.meals " +
            "where o.orderDate > :from and o.orderDate < :to")
    List<OnlineOrderEntity> getOrdersByDate(@Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to);
}
