package pl.restaurant.supplyservice.business.calculator;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class VolumeUnitCalculator extends UnitCalculator {
    public VolumeUnitCalculator() {
        this.converter = new LinkedHashMap<>();
        this.converter.put("l", BigDecimal.valueOf(1000.000));
        this.converter.put("ml", BigDecimal.valueOf(1.000));
    }
}
