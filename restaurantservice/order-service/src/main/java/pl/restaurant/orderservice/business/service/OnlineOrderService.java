package pl.restaurant.orderservice.business.service;

import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

public interface OnlineOrderService {
    OnlineOrderEntity getOrder(Long orderId);

    String getOrderDescription(OnlineOrderEntity order);

    void reserveOrder(OnlineOrder onlineOrder);

    void updateOrder(Long orderId, OnlineOrder order);
}
