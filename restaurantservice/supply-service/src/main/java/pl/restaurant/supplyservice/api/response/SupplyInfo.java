package pl.restaurant.supplyservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplyInfo {
    private Long restaurantId;
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal quantity;
    private Unit unit;
}
