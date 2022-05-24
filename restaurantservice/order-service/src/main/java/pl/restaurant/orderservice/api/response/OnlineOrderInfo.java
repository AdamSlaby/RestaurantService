package pl.restaurant.orderservice.api.response;

import lombok.*;
import pl.restaurant.orderservice.api.request.Address;
import pl.restaurant.orderservice.data.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlineOrderInfo {
    private Long id;
    private RestaurantShortInfo restaurantInfo;
    private String name;
    private String surname;
    private String email;
    private String phoneNr;
    private Address address;
    private Integer floor;
    private LocalDateTime orderDate;
    private boolean isPaid;
    private LocalDateTime deliveryDate;
    private PaymentMethod paymentMethod;
    private List<OrderInfo> orders;
    private BigDecimal price;
}
