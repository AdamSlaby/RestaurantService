package pl.restaurant.orderservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.orderservice.api.response.OrderInfo;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

@UtilityClass
public class OrderMapper {

    public static OrderInfo mapDataToObject(OnlineOrderMealEntity order) {
        return new OrderInfo().builder()
                .dishId(order.getId().getMealId())
                .name(order.getMealName())
                .amount(order.getQuantity())
                .price(order.getPrice())
                .build();
    }

    public static OrderInfo mapDataToObject(RestaurantOrderMealEntity order) {
        return new OrderInfo().builder()
                .dishId(order.getId().getMealId())
                .name(order.getMealName())
                .amount(order.getQuantity())
                .price(order.getPrice())
                .build();
    }
}
