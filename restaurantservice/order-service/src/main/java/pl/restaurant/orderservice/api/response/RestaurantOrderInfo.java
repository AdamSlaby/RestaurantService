package pl.restaurant.orderservice.api.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrderInfo {
    private Long id;
    private Long tableId;
    private RestaurantShortInfo restaurantInfo;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private List<OrderInfo> orders;
    private BigDecimal price;
}
