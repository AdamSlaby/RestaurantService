package pl.restaurant.mailservice.api.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private Integer dishId;
    private String name;
    private Integer amount;
    private BigDecimal price;
}
