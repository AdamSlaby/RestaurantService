package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealShortInfo {
    private Integer id;
    private String type;
    private String name;
    private BigDecimal price;
}
