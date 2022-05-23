package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.util.List;
import java.util.Optional;

public interface UnitRepo extends JpaRepository<UnitEntity, Integer> {
    boolean existsAllByUnitIdIn(List<Integer> unitIds);

    Optional<UnitEntity> findByName(String name);

    @Query("select new pl.restaurant.supplyservice.api.response." +
            "Unit(u.unitId, u.name) " +
            "from UnitEntity u")
    List<Unit> getAllUnits();
}
