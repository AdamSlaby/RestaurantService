package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.response.Unit;
import pl.restaurant.menuservice.data.entity.UnitEntity;

import java.util.List;

public interface UnitService {
    List<Unit> getAllUnits();

    UnitEntity getUnit(Integer unitId, int index);
}
