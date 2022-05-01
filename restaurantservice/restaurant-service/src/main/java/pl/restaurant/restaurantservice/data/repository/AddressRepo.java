package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
}
