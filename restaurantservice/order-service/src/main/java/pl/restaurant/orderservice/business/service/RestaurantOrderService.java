package pl.restaurant.orderservice.business.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantOrderInfo;

import java.util.List;

public interface RestaurantOrderService {
    Page<OrderShortInfo> getOrderList(OrderFilters filters, Pageable pageable);
    RestaurantOrderInfo getOrderInfo(Long orderId);
    List<ActiveOrder> getActiveOrders(Long restaurantId);
    void addRestaurantOrder(RestaurantOrder order);
    void updateOrder(Long orderId, RestaurantOrder order);

    void completeOrder(Long orderId);
}
