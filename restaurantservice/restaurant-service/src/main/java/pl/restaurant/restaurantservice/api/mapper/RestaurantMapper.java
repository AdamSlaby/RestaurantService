package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.data.entity.Restaurant;

@UtilityClass
public class RestaurantMapper {
    public RestaurantShortInfo mapRestaurantToShortInfo(Restaurant restaurant) {
        return new RestaurantShortInfo().builder()
                .restaurantId(restaurant.getRestaurantId())
                .city(restaurant.getAddress().getCity())
                .street(restaurant.getAddress().getStreet())
                .build();
    }

    public RestaurantInfo mapRestaurantToInfo(Restaurant restaurant) {
        return new RestaurantInfo().builder()
                .email(restaurant.getEmail())
                .phoneNr(restaurant.getPhoneNr())
                .address(AddressMapper.mapDataToObject(restaurant.getAddress()))
                .build();
    }

    public pl.restaurant.restaurantservice.api.request.Restaurant mapDataToObject(Restaurant restaurant) {
        return new pl.restaurant.restaurantservice.api.request.Restaurant().builder()
                .restaurantId(restaurant.getRestaurantId())
                .phoneNr(restaurant.getPhoneNr())
                .email(restaurant.getEmail())
                .deliveryFee(restaurant.getDeliveryFee())
                .minimalDeliveryPrice(restaurant.getMinimalDeliveryPrice())
                .address(AddressMapper.mapDataToObject(restaurant.getAddress()))
                .openingHours(OpeningHourMapper.mapDataToObject(restaurant.getOpeningHours()))
                .tables(TableMapper.mapDataToObject(restaurant.getTables()))
                .build();
    }
}
