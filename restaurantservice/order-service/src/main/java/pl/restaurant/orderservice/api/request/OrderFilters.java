package pl.restaurant.orderservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilters {
    private Long restaurantId;
    private Long orderId;
    private LocalDate orderDate;
    private Boolean isCompleted;
    @Valid
    private SortEvent sortEvent;
    private int pageNr;
}
