package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientView {
    private Integer id;
    private String name;
    private BigDecimal amount;
    private Unit unit;
}
