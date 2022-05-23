package pl.restaurant.supplyservice.business.service;

import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.util.List;

public interface UnitService {
    boolean validateUnitIds(List<Integer> unitIds);

    UnitEntity getUnit(Integer unitId, String fieldName);

    UnitEntity getUnit(String unit);

    List<Unit> getAllUnits();
}
