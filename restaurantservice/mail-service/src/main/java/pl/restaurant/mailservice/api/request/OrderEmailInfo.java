package pl.restaurant.mailservice.api.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
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
