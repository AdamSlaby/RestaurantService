package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.UnitEntity;

public interface UnitRepo extends JpaRepository<UnitEntity, Integer> {
}
