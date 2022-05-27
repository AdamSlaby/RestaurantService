package pl.restaurant.orderservice.api.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveOrder {
    private Long id;
    private List<DishShortInfo> dishesInfo;
    private String orderType;
    private LocalDateTime orderDate;
}
