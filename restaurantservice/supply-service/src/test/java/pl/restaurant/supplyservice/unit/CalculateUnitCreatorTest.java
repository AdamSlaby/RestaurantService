package pl.restaurant.supplyservice.unit;

import org.junit.jupiter.api.Test;
import pl.restaurant.supplyservice.business.calculator.CalculateUnitCreator;
import pl.restaurant.supplyservice.business.calculator.MassUnitCalculator;
import pl.restaurant.supplyservice.business.calculator.UnitCalculator;
import pl.restaurant.supplyservice.business.calculator.VolumeUnitCalculator;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateUnitCreatorTest {

    @Test
    public void createMassUnitCalculatorSuccess() {
        //given
        UnitEntity unit = new UnitEntity().builder()
                .name("kg")
                .build();
        UnitEntity unit2 = new UnitEntity().builder()
                .name("g")
                .build();
        //when
        CalculateUnitCreator creator = new CalculateUnitCreator();
        UnitCalculator unitCalculator = creator.create(unit, unit2);
        //then
        assertTrue(unitCalculator instanceof MassUnitCalculator);
    }

    @Test
    public void createVolumeUnitCalculatorSuccess() {
        //given
        UnitEntity unit = new UnitEntity().builder()
                .name("l")
                .build();
        UnitEntity unit2 = new UnitEntity().builder()
                .name("ml")
                .build();
        //when
        CalculateUnitCreator creator = new CalculateUnitCreator();
        UnitCalculator unitCalculator = creator.create(unit, unit2);
        //then
        assertTrue(unitCalculator instanceof VolumeUnitCalculator);
    }

    @Test
    public void createUnitCalculatorFailureTwoDifferentUnits() {
        //given
        UnitEntity unit = new UnitEntity().builder()
                .name("kg")
                .build();
        UnitEntity unit2 = new UnitEntity().builder()
                .name("ml")
                .build();
        //when
        CalculateUnitCreator creator = new CalculateUnitCreator();
        //then
        assertThrows(RuntimeException.class, () -> creator.create(unit, unit2));
    }

    @Test
    public void createUnitCalculatorFailureUnknownUnits() {
        //given
        UnitEntity unit = new UnitEntity().builder()
                .name("cm")
                .build();
        UnitEntity unit2 = new UnitEntity().builder()
                .name("mm")
                .build();
        //when
        CalculateUnitCreator creator = new CalculateUnitCreator();
        //then
        assertThrows(RuntimeException.class, () -> creator.create(unit, unit2));
    }
}
