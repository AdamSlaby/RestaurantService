package pl.restaurant.menuservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientView {
    private Integer id;
    private String name;
    private BigDecimal amount;
    private Unit unit;
}
