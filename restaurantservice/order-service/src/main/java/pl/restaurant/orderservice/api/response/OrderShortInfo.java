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

    public OrderShortInfo(Long id, String type, BigDecimal price, LocalDateTime orderDate, LocalDateTime dateTime) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.orderDate = orderDate;
        this.isCompleted = dateTime != null;
    }
}
