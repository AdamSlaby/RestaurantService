package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.AddressEntity;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
}
