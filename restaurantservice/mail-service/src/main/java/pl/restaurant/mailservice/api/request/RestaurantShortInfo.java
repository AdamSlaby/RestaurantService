package pl.restaurant.mailservice.api.request;

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

    @Override
    public String toString() {
        return city + " ul. " + street;
    }
}
