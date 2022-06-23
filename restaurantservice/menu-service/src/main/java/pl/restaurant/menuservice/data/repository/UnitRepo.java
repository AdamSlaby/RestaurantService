package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.menuservice.api.response.Unit;
import pl.restaurant.menuservice.data.entity.UnitEntity;

import java.util.List;
import java.util.Optional;

public interface UnitRepo extends JpaRepository<UnitEntity, Integer> {
    @Query("select new pl.restaurant.menuservice.api.response." +
            "Unit(u.unitId, u.name) " +
            "from UnitEntity u")
    List<Unit> getAllUnits();

    Optional<UnitEntity> findByName(String name);
}
