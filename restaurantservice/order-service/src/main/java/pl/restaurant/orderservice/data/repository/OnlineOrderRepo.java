package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.util.Optional;

public interface OnlineOrderRepo extends JpaRepository<OnlineOrderEntity, Long> {
    @EntityGraph(attributePaths = {"meals", "address"})
    Optional<OnlineOrderEntity> getByOrderId(Long orderId);
}
