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
public class DishOrderView {
    private Integer id;
    private String imageUrl;
    private String name;
    private String type;
    private String ingredients;
    private Integer amount;
    private BigDecimal price;
}
