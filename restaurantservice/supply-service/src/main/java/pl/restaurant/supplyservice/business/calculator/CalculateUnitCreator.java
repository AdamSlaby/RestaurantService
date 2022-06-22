package pl.restaurant.supplyservice.business.calculator;

import pl.restaurant.supplyservice.data.entity.UnitEntity;

public class CalculateUnitCreator implements Creator<UnitCalculator, UnitEntity> {
    @Override
    public UnitCalculator create(UnitEntity value1, UnitEntity value2) {
        UnitCalculator unitCalculator = new MassUnitCalculator();
        if (unitCalculator.isUnit(value1) && unitCalculator.isUnit(value2))
            return unitCalculator;
        unitCalculator = new VolumeUnitCalculator();
        if (unitCalculator.isUnit(value1) && unitCalculator.isUnit(value2))
            return unitCalculator;
        throw new RuntimeException("Podana jednostka jest nieprawidłowa");
    }
}
