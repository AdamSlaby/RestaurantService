package pl.restaurant.orderservice.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListView {
    private Long totalElements;
    private List<OrderShortInfo> orders;
}
