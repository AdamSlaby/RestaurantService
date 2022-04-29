package pl.restaurant.restaurantservice.business.service;

import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;

import java.util.List;

public interface RestaurantService {
    RestaurantInfo getRestaurantInfo(Long id);

    Restaurant getRestaurantDetailedInfo(Long id);

    List<RestaurantShortInfo> getAllRestaurants();

    void addRestaurant(Restaurant restaurant);

    Restaurant updateRestaurant(Long id, Restaurant restaurant);
}
