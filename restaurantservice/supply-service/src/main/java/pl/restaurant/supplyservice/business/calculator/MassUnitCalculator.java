package pl.restaurant.supplyservice.business.calculator;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class MassUnitCalculator extends UnitCalculator {
    public MassUnitCalculator() {
        this.converter = new LinkedHashMap<>();
        this.converter.put("kg", BigDecimal.valueOf(1000.000));
        this.converter.put("dag", BigDecimal.valueOf(10.000));
        this.converter.put("g", BigDecimal.valueOf(1.000));
    }
}
