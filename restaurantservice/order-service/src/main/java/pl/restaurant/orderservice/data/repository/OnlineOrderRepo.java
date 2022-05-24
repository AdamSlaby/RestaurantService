package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

public interface OnlineOrderRepo extends JpaRepository<OnlineOrderEntity, Long> {
}
