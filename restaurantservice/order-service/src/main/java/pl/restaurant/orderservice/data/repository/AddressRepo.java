package pl.restaurant.orderservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.orderservice.data.entity.AddressEntity;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
}
