package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.util.List;
import java.util.Optional;

public interface UnitRepo extends JpaRepository<UnitEntity, Integer> {
    boolean existsAllByUnitIdIn(List<Integer> unitIds);

    Optional<UnitEntity> findByName(String name);
}
