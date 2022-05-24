package pl.restaurant.orderservice.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private Integer dishId;
    private String name;
    private Integer amount;
    private BigDecimal price;
}
