package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

public interface UnitRepo extends JpaRepository<UnitEntity, Integer> {

}
