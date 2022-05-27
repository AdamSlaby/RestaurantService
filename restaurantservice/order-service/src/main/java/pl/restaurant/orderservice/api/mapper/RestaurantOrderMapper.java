package pl.restaurant.orderservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.orderservice.api.request.Order;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.DishShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantOrderInfo;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;
import pl.restaurant.orderservice.data.entity.OrderMealId;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static pl.restaurant.orderservice.business.service.order.RestaurantOrderServiceImpl.RESTAURANT_TYPE;

@UtilityClass
public class RestaurantOrderMapper {
    public static RestaurantOrderInfo mapDataToInfo(RestaurantOrderEntity order, RestaurantShortInfo restaurant) {
        return new RestaurantOrderInfo().builder()
                .id(order.getOrderId())
                .tableId(order.getTableId())
                .restaurantInfo(restaurant)
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .orders(order.getMeals().stream()
                        .map(OrderMapper::mapDataToObject)
                        .collect(Collectors.toList()))
                .price(order.getPrice())
                .build();
    }

    public static ActiveOrder mapDataToActive(RestaurantOrderEntity order) {
        return new ActiveOrder().builder()
                .id(order.getOrderId())
                .dishesInfo(order.getMeals().stream()
                        .map(el -> new DishShortInfo(el.getMealName(), el.getQuantity()))
                        .collect(Collectors.toList()))
                .orderType(RESTAURANT_TYPE)
                .orderDate(order.getOrderDate())
                .build();
    }

    public static RestaurantOrderEntity mapObjectToData(RestaurantOrder order) {
        return new RestaurantOrderEntity().builder()
                .restaurantId(order.getRestaurantId())
                .tableId(order.getTableId())
                .orderDate(LocalDateTime.now())
                .price(order.getPrice())
                .deliveryDate(null)
                .build();
    }

    public static RestaurantOrderMealEntity mapOrderToData(Order order, RestaurantOrderEntity restaurantOrder) {
        return new RestaurantOrderMealEntity().builder()
                .id(new OrderMealId(restaurantOrder.getOrderId(), order.getDishId()))
                .mealName(order.getName())
                .price(order.getPrice())
                .quantity(order.getAmount())
                .build();
    }
}
