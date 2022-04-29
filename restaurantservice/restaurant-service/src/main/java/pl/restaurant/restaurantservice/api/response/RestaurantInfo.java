package pl.restaurant.restaurantservice.api.response;

import lombok.*;
import pl.restaurant.restaurantservice.api.request.Address;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    private String email;
    private String phoneNr;
    private Address address;
}
