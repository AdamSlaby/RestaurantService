package pl.restaurant.orderservice.api.response;

import lombok.*;
import pl.restaurant.orderservice.api.request.Address;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEmailInfo {
    private Long id;
    private RestaurantShortInfo restaurantInfo;
    private String name;
    private String surname;
    private String email;
    private String phoneNr;
    private Address address;
    private Integer floor;
    private String paymentMethod;
    private List<OrderInfo> orders;
    private BigDecimal price;
}
