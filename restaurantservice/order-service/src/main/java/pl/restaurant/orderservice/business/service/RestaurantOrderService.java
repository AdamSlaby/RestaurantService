package pl.restaurant.orderservice.business.service;

import pl.restaurant.orderservice.api.request.RestaurantOrder;

public interface RestaurantOrderService {
    void addRestaurantOrder();

    void updateOrder(Long orderId, RestaurantOrder order);
}
