package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.OpeningHour;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;

import java.util.List;

@UtilityClass
public class RestaurantMapper {
    public RestaurantShortInfo mapRestaurantToShortInfo(RestaurantEntity restaurant) {
        return new RestaurantShortInfo().builder()
                .restaurantId(restaurant.getRestaurantId())
                .city(restaurant.getAddress().getCity())
                .street(restaurant.getAddress().getStreet())
                .build();
    }

    public RestaurantInfo mapRestaurantToInfo(RestaurantEntity restaurantEntity, List<OpeningHour> hours) {
        return new RestaurantInfo().builder()
                .email(restaurantEntity.getEmail())
                .phoneNr(restaurantEntity.getPhoneNr())
                .address(AddressMapper.mapDataToObject(restaurantEntity.getAddress()))
                .openingHours(hours)
                .build();
    }

    public Restaurant mapDataToObject(RestaurantEntity restaurantEntity, List<OpeningHour> hours) {
        return new Restaurant().builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .phoneNr(restaurantEntity.getPhoneNr())
                .email(restaurantEntity.getEmail())
                .deliveryFee(restaurantEntity.getDeliveryFee())
                .minimalDeliveryPrice(restaurantEntity.getMinimalDeliveryPrice())
                .address(AddressMapper.mapDataToObject(restaurantEntity.getAddress()))
                .openingHours(hours)
                .tables(TableMapper.mapDataToObject(restaurantEntity.getTables()))
                .build();
    }

    public RestaurantEntity mapObjectToData(Restaurant restaurant, AddressEntity address) {
        return new RestaurantEntity().builder()
                .email(restaurant.getEmail())
                .phoneNr(restaurant.getPhoneNr())
                .address(address)
                .deliveryFee(restaurant.getDeliveryFee())
                .minimalDeliveryPrice(restaurant.getMinimalDeliveryPrice())
                .build();
    }
}
