package pl.restaurant.orderservice.business.service;

import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderListView;
import pl.restaurant.orderservice.api.response.OrdersInfo;

import java.util.List;

public interface OrderService {
    OrderListView getOrderList(OrderFilters filters);

    OrdersInfo getOrderInfo(Long orderId, String type);

    List<ActiveOrder> getActiveOrders(Long restaurantId);

}
