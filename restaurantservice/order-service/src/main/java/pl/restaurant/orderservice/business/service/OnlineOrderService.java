package pl.restaurant.orderservice.business.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OnlineOrderInfo;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.util.List;

public interface OnlineOrderService {
    OnlineOrderEntity getOrder(Long orderId);
    OnlineOrderInfo getOrderInfo(Long orderId);
    Page<OrderShortInfo> getOrderList(OrderFilters filters, Pageable pageable);
    List<ActiveOrder> getActiveOrders(Long restaurantId);

    String getOrderDescription(OnlineOrderEntity order);

    Long reserveOrder(OnlineOrder onlineOrder);

    void rollbackOrder(Long orderId);

    void changePaymentStatus(Long orderId);

    void completeOrder(Long orderId);
}
