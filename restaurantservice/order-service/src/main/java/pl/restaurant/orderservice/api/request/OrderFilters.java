package pl.restaurant.orderservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilters {
    private Long restaurantId;
    private Long orderId;

    @NotEmpty(message = "Typ zamównienia nie może być pusty")
    private String type;
    private LocalDateTime orderDate;
    private Boolean isCompleted;

    @Valid
    private SortEvent sortEvent;
    private int pageNr;
}
