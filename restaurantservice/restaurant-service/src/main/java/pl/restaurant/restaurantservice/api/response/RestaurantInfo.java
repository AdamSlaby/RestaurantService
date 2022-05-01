package pl.restaurant.restaurantservice.api.response;

import lombok.*;
import pl.restaurant.restaurantservice.api.request.Address;
import pl.restaurant.restaurantservice.api.request.OpeningHour;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    private String email;
    private String phoneNr;
    private Address address;
    private List<OpeningHour> openingHours;
}
