package pl.restaurant.supplyservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.restaurant.supplyservice.api.request.TaxType;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodView {
    private Long id;
    private Integer ingredientId;
    private String ingredient;
    private BigDecimal quantity;
    private Unit unit;
    private BigDecimal unitNetPrice;
    private BigDecimal discount;
    private BigDecimal netPrice;
    private TaxType taxType;
    private BigDecimal taxPrice;
}
