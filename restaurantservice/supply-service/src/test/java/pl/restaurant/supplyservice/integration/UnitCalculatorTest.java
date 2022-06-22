package pl.restaurant.supplyservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Operation;
import pl.restaurant.supplyservice.business.calculator.MassUnitCalculator;
import pl.restaurant.supplyservice.business.calculator.Result;
import pl.restaurant.supplyservice.business.calculator.UnitCalculator;
import pl.restaurant.supplyservice.business.calculator.VolumeUnitCalculator;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitCalculatorTest {

    @Test
    public void calculateMassUnitsSuccessAdd() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(101), "g");
        BigDecimal quantity = BigDecimal.valueOf(55);
        UnitEntity unit = getUnit("dag");
        Operation operation = Operation.ADD;
        //when
        UnitCalculator calculator = new MassUnitCalculator();
        Result result = calculator.calculateUnits(supply, quantity, unit, operation);
        //then
        assertThat(result.getResult()).isEqualTo(BigDecimal.valueOf(65.1).setScale(3, RoundingMode.UNNECESSARY));
        assertThat(result.getUnit()).isEqualTo("dag");
    }

    @Test
    public void calculateMassUnitsSuccessSubtract() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(1), "kg");
        BigDecimal quantity = BigDecimal.valueOf(90);
        UnitEntity unit = getUnit("dag");
        Operation operation = Operation.SUBTRACT;
        //when
        UnitCalculator calculator = new MassUnitCalculator();
        Result result = calculator.calculateUnits(supply, quantity, unit, operation);
        //then
        assertThat(result.getResult()).isEqualTo(BigDecimal.valueOf(10).setScale(3, RoundingMode.UNNECESSARY));
        assertThat(result.getUnit()).isEqualTo("dag");
    }

    @Test
    public void calculateMassUnitsFailure() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(1), "kg");
        BigDecimal quantity = BigDecimal.valueOf(90);
        UnitEntity unit = getUnit("dag");
        Operation operation = Operation.DIVIDE;
        //when
        UnitCalculator calculator = new MassUnitCalculator();
        //then
        assertThrows(RuntimeException.class, () -> calculator.calculateUnits(supply, quantity, unit, operation));
    }

    @Test
    public void calculateVolumeUnitsSuccessAdd() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(552), "ml");
        BigDecimal quantity = BigDecimal.valueOf(555);
        UnitEntity unit = getUnit("ml");
        Operation operation = Operation.ADD;
        //when
        UnitCalculator calculator = new VolumeUnitCalculator();
        Result result = calculator.calculateUnits(supply, quantity, unit, operation);
        //then
        assertThat(result.getResult()).isEqualTo(BigDecimal.valueOf(1.107).setScale(3, RoundingMode.UNNECESSARY));
        assertThat(result.getUnit()).isEqualTo("l");
    }

    @Test
    public void calculateVolumeUnitsSuccessSubtract() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(1.05), "l");
        BigDecimal quantity = BigDecimal.valueOf(1);
        UnitEntity unit = getUnit("l");
        Operation operation = Operation.SUBTRACT;
        //when
        UnitCalculator calculator = new VolumeUnitCalculator();
        Result result = calculator.calculateUnits(supply, quantity, unit, operation);
        //then
        assertThat(result.getResult()).isEqualTo(BigDecimal.valueOf(50).setScale(3, RoundingMode.UNNECESSARY));
        assertThat(result.getUnit()).isEqualTo("ml");
    }

    @Test
    public void calculateVolumeUnitsFailure() {
        //given
        SupplyEntity supply = getSupply(BigDecimal.valueOf(1.05), "l");
        BigDecimal quantity = BigDecimal.valueOf(1);
        UnitEntity unit = getUnit("l");
        Operation operation = Operation.DIVIDE;
        //when
        UnitCalculator calculator = new VolumeUnitCalculator();
        //then
        assertThrows(RuntimeException.class, () -> calculator.calculateUnits(supply, quantity, unit, operation));
    }

    private SupplyEntity getSupply(BigDecimal quantity, String unit) {
        return new SupplyEntity().builder()
                .quantity(quantity)
                .unit(getUnit(unit))
                .build();
    }

    private UnitEntity getUnit(String unit) {
        return new UnitEntity().builder()
                .name(unit)
                .build();
    }
}
