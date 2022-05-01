package pl.restaurant.restaurantservice.business.service;

import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;

import java.util.List;

public interface RestaurantService {
    RestaurantInfo getRestaurantInfo(Long id);

    Table getRestaurantTable(int seatsNr, long restaurantId);

    Restaurant getRestaurantDetailedInfo(Long id);

    List<RestaurantShortInfo> getAllRestaurants();

    void addRestaurant(Restaurant restaurant);

    void updateRestaurant(Long id, Restaurant restaurant);

    void removeTableFromRestaurant(long tableId, long restaurantId);
}
