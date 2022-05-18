package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private Integer id;
    private String name;
    private String type;
    private String ingredients;
    private BigDecimal price;
    private boolean isBest;
}
