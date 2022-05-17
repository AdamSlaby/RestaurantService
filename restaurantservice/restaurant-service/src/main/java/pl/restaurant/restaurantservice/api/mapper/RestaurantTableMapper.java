package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableId;

@UtilityClass
public class RestaurantTableMapper {
    public RestaurantTableEntity mapToData(RestaurantEntity restaurant, FoodTableEntity table) {
        return new RestaurantTableEntity().builder()
                .restaurantTableId(new RestaurantTableId(restaurant.getRestaurantId(), table.getTableId()))
                .table(table)
                .restaurant(restaurant)
                .build();
    }
}
