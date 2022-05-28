package pl.restaurant.orderservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.api.request.Order;
import pl.restaurant.orderservice.api.response.*;
import pl.restaurant.orderservice.data.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static pl.restaurant.orderservice.business.service.order.OnlineOrderServiceImpl.ONLINE_TYPE;
import static pl.restaurant.orderservice.business.service.order.OnlineOrderServiceImpl.TIME_TO_PAID;

@UtilityClass
public class OnlineOrderMapper {

    public static OnlineOrderInfo mapDataToInfo(OnlineOrderEntity order, RestaurantShortInfo restaurant) {
        return new OnlineOrderInfo().builder()
                .id(order.getOrderId())
                .restaurantInfo(restaurant)
                .name(order.getName())
                .surname(order.getSurname())
                .email(order.getEmail())
                .phoneNr(order.getPhoneNr())
                .address(AddressMapper.mapDataToObject(order.getAddress()))
                .floor(order.getFloor())
                .orderDate(order.getOrderDate())
                .isPaid(order.isPaid())
                .deliveryDate(order.getDeliveryDate())
                .paymentMethod(order.getPaymentMethod())
                .orders(order.getMeals().stream()
                        .map(OrderMapper::mapDataToObject)
                        .collect(Collectors.toList()))
                .price(order.getPrice())
                .build();
    }

    public static ActiveOrder mapDataToActive(OnlineOrderEntity order) {
        return new ActiveOrder().builder()
                .id(order.getOrderId())
                .dishesInfo(order.getMeals().stream()
                        .map(el -> new DishShortInfo(el.getMealName(), el.getQuantity()))
                        .collect(Collectors.toList()))
                .orderType(ONLINE_TYPE)
                .orderDate(order.getOrderDate())
                .build();
    }

    public static OnlineOrderEntity mapObjectToData(OnlineOrder order, BigDecimal price, AddressEntity address) {
        LocalDateTime timeToPaid = order.getPaymentMethod() == PaymentMethod.CASH ||
                order.getPaymentMethod() == PaymentMethod.CARD ? LocalDateTime.now().plusMinutes(TIME_TO_PAID) : null;
        return new OnlineOrderEntity().builder()
                .restaurantId(order.getRestaurantId())
                .name(order.getName())
                .surname(order.getSurname())
                .email(order.getEmail())
                .phoneNr(order.getPhoneNr())
                .floor(order.getFloor())
                .orderDate(LocalDateTime.now())
                .timeToPaid(timeToPaid)
                .isPaid(false)
                .deliveryDate(null)
                .paymentMethod(order.getPaymentMethod())
                .price(price)
                .address(address)
                .build();
    }

    public static OnlineOrderMealEntity mapOrderToData(Order order, OnlineOrderEntity onlineOrder) {
        return new OnlineOrderMealEntity().builder()
                .id(new OrderMealId(onlineOrder.getOrderId(), order.getDishId()))
                .order(onlineOrder)
                .mealName(order.getName())
                .price(order.getPrice())
                .quantity(order.getAmount())
                .build();
    }

    public static Order mapDataToOrder(OnlineOrderMealEntity order) {
        return Order.builder()
                .dishId(order.getId().getMealId())
                .name(null)
                .amount(order.getQuantity())
                .price(null)
                .build();
    }

    public static OrderEmailInfo mapDataToEmailInfo(OnlineOrderEntity order, RestaurantShortInfo restaurant) {
        return new OrderEmailInfo().builder()
                .id(order.getOrderId())
                .restaurantInfo(restaurant)
                .name(order.getName())
                .surname(order.getSurname())
                .email(order.getEmail())
                .phoneNr(order.getPhoneNr())
                .address(AddressMapper.mapDataToObject(order.getAddress()))
                .floor(order.getFloor())
                .paymentMethod(order.getPaymentMethod().toString())
                .orders(order.getMeals().stream()
                        .map(OrderMapper::mapDataToObject)
                        .collect(Collectors.toList()))
                .price(order.getPrice())
                .build();
    }
}
