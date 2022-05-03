package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.employeeservice.data.entity.AddressEntity;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
}
