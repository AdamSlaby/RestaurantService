package pl.restaurant.orderservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.orderservice.api.mapper.RestaurantOrderMapper;
import pl.restaurant.orderservice.api.request.Order;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantOrderInfo;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;
import pl.restaurant.orderservice.business.exception.CannotUpdateOrderException;
import pl.restaurant.orderservice.business.exception.InvalidOrderException;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.business.exception.TableNotFoundException;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;
import pl.restaurant.orderservice.data.repository.RestaurantOrderMealRepo;
import pl.restaurant.orderservice.data.repository.RestaurantOrderRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantOrderServiceImpl implements RestaurantOrderService {
    public static final String RESTAURANT_TYPE = "Restaurant";
    private RestaurantOrderRepo orderRepo;
    private RestaurantOrderMealRepo mealRepo;
    private MenuServiceClient menuClient;
    private RestaurantServiceClient restaurantClient;

    @Override
    public Page<OrderShortInfo> getOrderList(OrderFilters filters, Pageable pageable) {
        LocalDateTime orderDate = filters.getOrderDate();
        LocalDateTime from = null;
        LocalDateTime to = null;
        if (orderDate != null) {
            from = orderDate.withHour(0).withMinute(0).withSecond(0);
            to = orderDate.withHour(23).withMinute(59).withSecond(59);
        }
        return orderRepo.getOrders(filters.getRestaurantId(), filters.getOrderId(), from, to,
                filters.getIsCompleted(), pageable);
    }

    @Override
    public RestaurantOrderInfo getOrderInfo(Long orderId) {
        RestaurantOrderEntity order = orderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
        RestaurantShortInfo restaurant = restaurantClient.getRestaurantShortInfo(order.getRestaurantId());
        return RestaurantOrderMapper.mapDataToInfo(order, restaurant);
    }

    @Override
    public List<ActiveOrder> getActiveOrders(Long restaurantId) {
        return orderRepo.getActiveOrders(restaurantId).stream()
                .map(RestaurantOrderMapper::mapDataToActive)
                .collect(Collectors.toList());
    }

    @Override
    public void addRestaurantOrder(RestaurantOrder restaurantOrder) {
        if (!restaurantClient.isRestaurantTableExists(restaurantOrder.getRestaurantId(), restaurantOrder.getTableId()))
            throw new TableNotFoundException();
        String error = menuClient.validateOrders(restaurantOrder.getRestaurantId(), restaurantOrder.getOrders());
        if (error != null)
            throw new InvalidOrderException(error);
        saveOrder(restaurantOrder);
    }

    @Transactional
    public void saveOrder(RestaurantOrder restaurantOrder) {
        RestaurantOrderEntity restaurantOrderEntity = orderRepo
                .save(RestaurantOrderMapper.mapObjectToData(restaurantOrder));
        for (Order order : restaurantOrder.getOrders())
            mealRepo.save(RestaurantOrderMapper.mapOrderToData(order, restaurantOrderEntity));
    }

    @Override
    public void updateOrder(Long orderId, RestaurantOrder order) {
        RestaurantOrderEntity restaurantOrderEntity = orderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (restaurantOrderEntity.getDeliveryDate() != null)
            throw new CannotUpdateOrderException();
        String error = menuClient.validateOrders(order.getRestaurantId(), order.getOrders());
        if (error != null)
            throw new InvalidOrderException(error);
        updateOrder(restaurantOrderEntity, order);
    }

    @Override
    public void completeOrder(Long orderId) {
        RestaurantOrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.setDeliveryDate(LocalDateTime.now());
        orderRepo.save(order);
    }

    @Transactional
    public void updateOrder(RestaurantOrderEntity restaurantOrderEntity, RestaurantOrder restaurantOrder) {
        restaurantOrderEntity.setPrice(restaurantOrder.getPrice());
        orderRepo.save(restaurantOrderEntity);
        for (Order order : restaurantOrder.getOrders())
            mealRepo.save(RestaurantOrderMapper.mapOrderToData(order, restaurantOrderEntity));
    }
}
