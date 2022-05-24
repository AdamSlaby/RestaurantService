package pl.restaurant.orderservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantShortInfo {
    private Long restaurantId;
    private String city;
    private String street;
}
