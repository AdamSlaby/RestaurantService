package pl.restaurant.restaurantservice.business.service;

import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantService {
    RestaurantInfo getRestaurantInfo(Long id);

    boolean isRestaurantExist(Long id);

    Table getRestaurantTable(int seatsNr, long restaurantId);

    Restaurant getRestaurantDetailedInfo(Long id);

    RestaurantShortInfo getRestaurantShortInfo(Long restaurantId);

    List<RestaurantShortInfo> getAllRestaurants();

    List<Long> getAllTables(Long restaurantId);

    void addRestaurant(Restaurant restaurant);

    void updateRestaurant(Long id, Restaurant restaurant);

    void removeTableFromRestaurant(long tableId, long restaurantId);

    boolean isRestaurantTableExist(Long restaurantId, Long tableId);

    BigDecimal getRestaurantDeliveryFee(Long id);
}
