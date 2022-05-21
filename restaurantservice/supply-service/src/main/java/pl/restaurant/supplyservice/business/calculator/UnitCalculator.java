package pl.restaurant.supplyservice.business.calculator;

import org.springframework.expression.Operation;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public abstract class UnitCalculator {
    Map<String, BigDecimal> converter;

    public Result calculateUnits(SupplyEntity supply, BigDecimal quantity, UnitEntity unit, Operation operation) {
        BigDecimal result = getResult(supply, quantity, unit, operation);
        for (Map.Entry<String, BigDecimal> entry: converter.entrySet()) {
            BigDecimal divide = result.divide(entry.getValue(), RoundingMode.UNNECESSARY);
            if (divide.compareTo(BigDecimal.ONE) >= 0)
                return new Result(divide, entry.getKey());
        }
        throw new RuntimeException("Exception has occurred. Implementation of calculate method is incorrect");
    }

    BigDecimal getResult(SupplyEntity supply, BigDecimal quantity, UnitEntity unit, Operation operation) {
        if (operation == Operation.ADD)
            return addUnits(supply, quantity, unit);
        if (operation == Operation.SUBTRACT)
            return subtractUnits(supply, quantity, unit);
        throw new RuntimeException("Operation not supported exception");
    }

    BigDecimal addUnits(SupplyEntity supply, BigDecimal quantity, UnitEntity unit) {
        return supply.getQuantity().multiply(converter.get(supply.getUnit().getName()))
                .add(quantity.multiply(converter.get(unit.getName())));
    }

    BigDecimal subtractUnits(SupplyEntity supply, BigDecimal quantity, UnitEntity unit) {
        return supply.getQuantity().multiply(converter.get(supply.getUnit().getName()))
                .subtract(quantity.multiply(converter.get(unit.getName())));
    }

    public boolean isUnit(UnitEntity unit) {
        return converter.containsKey(unit.getName());
    }
}
