package pl.restaurant.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderShortInfo {
    private Long id;
    private String type;
    private BigDecimal price;
    private LocalDateTime orderDate;
    private boolean isCompleted;
}
