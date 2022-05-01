package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.restaurantservice.data.entity.OpeningHourEntity;

public interface OpeningHourRepo extends JpaRepository<OpeningHourEntity, Long> {
}
