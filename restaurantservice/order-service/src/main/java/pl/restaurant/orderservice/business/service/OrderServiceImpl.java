package pl.restaurant.orderservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.request.SortDirection;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderListView;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.OrdersInfo;
import pl.restaurant.orderservice.business.exception.ColumnNotFoundException;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;

import java.util.List;

import static pl.restaurant.orderservice.business.service.OnlineOrderServiceImpl.ONLINE_TYPE;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final static int AMOUNT = 10;
    private OnlineOrderService onlineService;
    private RestaurantOrderService restaurantService;

    @Override
    public OrderListView getOrderList(OrderFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<OrderShortInfo> page;
        if (filters.getType().equals(ONLINE_TYPE))
            page = onlineService.getOrderList(filters, pageable);
        else
            page = restaurantService.getOrderList(filters, pageable);
        return new OrderListView().builder()
                .totalElements(page.getTotalElements())
                .orders(page.getContent())
                .build();
    }

    @Override
    public OrdersInfo getOrderInfo(Long orderId, String type) {
        if (type.equals(ONLINE_TYPE))
            return new OrdersInfo(null, onlineService.getOrderInfo(orderId));
        else
            return new OrdersInfo(restaurantService.getOrderInfo(orderId), null);
    }

    @Override
    public List<ActiveOrder> getActiveOrders(Long restaurantId) {
        List<ActiveOrder> orders = onlineService.getActiveOrders(restaurantId);
        orders.addAll(restaurantService.getActiveOrders(restaurantId));
        return orders;
    }

    private Pageable mapSortEventToPageable(OrderFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                OnlineOrderEntity.class.getDeclaredField(column);
                if (filters.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filters.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }
}
