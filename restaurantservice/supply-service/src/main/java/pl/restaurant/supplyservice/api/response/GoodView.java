package pl.restaurant.supplyservice.api.response;

import lombok.*;
import pl.restaurant.supplyservice.api.request.TaxType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodView {
    private Long id;
    private Integer ingredientId;
    private BigDecimal quantity;
    private Integer unitId;
    private BigDecimal unitNetPrice;
    private BigDecimal discount;
    private BigDecimal netPrice;
    private TaxType taxType;
    private BigDecimal taxPrice;
}
