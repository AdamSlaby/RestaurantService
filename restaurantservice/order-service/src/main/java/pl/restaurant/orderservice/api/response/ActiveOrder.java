package pl.restaurant.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveOrder {
    private Long id;
    private List<DishShortInfo> dishInfo;
    private String orderType;
    private LocalDateTime orderDate;
}
