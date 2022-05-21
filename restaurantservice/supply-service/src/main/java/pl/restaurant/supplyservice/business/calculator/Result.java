package pl.restaurant.supplyservice.business.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Result {
    private BigDecimal result;
    private String unit;
}
