package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishOrderView {
    private Integer id;
    private String imageUrl;
    private String name;
    private String type;
    private String ingredients;
    private Integer amount;
    private BigDecimal price;
}
