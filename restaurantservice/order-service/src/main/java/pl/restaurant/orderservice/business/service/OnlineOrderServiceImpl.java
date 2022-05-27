package pl.restaurant.orderservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.orderservice.api.mapper.AddressMapper;
import pl.restaurant.orderservice.api.mapper.OnlineOrderMapper;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.api.request.Order;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OnlineOrderInfo;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;
import pl.restaurant.orderservice.business.exception.CannotCompleteOrderException;
import pl.restaurant.orderservice.business.exception.InvalidOrderException;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.orderservice.data.entity.AddressEntity;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;
import pl.restaurant.orderservice.data.repository.AddressRepo;
import pl.restaurant.orderservice.data.repository.OnlineOrderMealRepo;
import pl.restaurant.orderservice.data.repository.OnlineOrderRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {
    public static final String ONLINE_TYPE = "Online";
    public static final int TIME_TO_PAID = 30;
    private OnlineOrderRepo orderRepo;
    private AddressRepo addressRepo;
    private OnlineOrderMealRepo mealRepo;
    private MenuServiceClient menuClient;
    private RestaurantServiceClient restaurantClient;

    @Override
    public OnlineOrderEntity getOrder(Long orderId) {
        return orderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public OnlineOrderInfo getOrderInfo(Long orderId) {
        OnlineOrderEntity order = orderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
        RestaurantShortInfo restaurant = restaurantClient.getRestaurantShortInfo(order.getRestaurantId());
        return OnlineOrderMapper.mapDataToInfo(order, restaurant);
    }

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
    public List<ActiveOrder> getActiveOrders(Long restaurantId) {
        return orderRepo.getActiveOrders(restaurantId).stream()
                .map(OnlineOrderMapper::mapDataToActive)
                .collect(Collectors.toList());
    }

    @Override
    public String getOrderDescription(OnlineOrderEntity order) {
        StringBuilder builder = new StringBuilder();
        builder.append("Imie i nazwisko: ").append(order.getName()).append(" ").append(order.getSurname()).append("\n");
        builder.append("Email: ").append(order.getEmail()).append("\n");
        builder.append("Metoda płatności: ").append(order.getPaymentMethod().toString()).append("\n");
        builder.append("Cena do zapłaty: ").append(order.getPrice().toString()).append("\n");
        return builder.toString();
    }

    @Override
    public Long reserveOrder(OnlineOrder onlineOrder) {
        if (!restaurantClient.isRestaurantExist(onlineOrder.getRestaurantId()))
            throw new RestaurantNotFoundException();
        String error = menuClient.validateOrders(onlineOrder.getRestaurantId(), onlineOrder.getOrders());
        if (error != null)
            throw new InvalidOrderException(error);
        return saveOrder(onlineOrder);
    }

    @Transactional
    public Long saveOrder(OnlineOrder onlineOrder) {
        BigDecimal price = BigDecimal.ZERO;
        for (Order order : onlineOrder.getOrders())
            price = price.add(order.getPrice());
        AddressEntity address = addressRepo.save(AddressMapper.mapObjectToData(onlineOrder.getAddress()));
        OnlineOrderEntity onlineOrderEntity = orderRepo
                .save(OnlineOrderMapper.mapObjectToData(onlineOrder, price, address));
        for (Order order : onlineOrder.getOrders())
            mealRepo.save(OnlineOrderMapper.mapOrderToData(order, onlineOrderEntity));
        return onlineOrderEntity.getOrderId();
    }

    @Override
    public void rollbackOrder(Long orderId) {
        OnlineOrderEntity order = orderRepo.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        rollbackOrder(order);
    }

    public void rollbackOrder(OnlineOrderEntity order) {
        List<Order> orders = order.getMeals().stream()
                .map(OnlineOrderMapper::mapDataToOrder)
                .collect(Collectors.toList());
        menuClient.rollbackOrderSupplies(order.getRestaurantId(), orders);
        deleteOrder(order);
    }

    @Transactional
    public void deleteOrder(OnlineOrderEntity order) {
        mealRepo.deleteMealsByOrderId(order.getOrderId());
        orderRepo.deleteById(order.getOrderId());
    }

    @Override
    public void changePaymentStatus(Long orderId) {
        OnlineOrderEntity order = orderRepo.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setPaid(true);
        orderRepo.save(order);
    }

    @Override
    public void completeOrder(Long orderId) {
        OnlineOrderEntity order = orderRepo.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (!order.isPaid())
            throw new CannotCompleteOrderException();
        order.setDeliveryDate(LocalDateTime.now());
        orderRepo.save(order);
    }

    //method will be invoked every 30 minutes (1000 milis * 60 sec * 30 min = 1800000 milis)
    @Scheduled(fixedRate = 1800000)
    public void checkIfOrderWasPaid() {
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<OnlineOrderEntity> orders = orderRepo.getOrdersByDate(from, to);
        for (OnlineOrderEntity order : orders) {
            if (isOrderUnpaid(order))
                rollbackOrder(order);
        }
    }


    private boolean isOrderUnpaid(OnlineOrderEntity order) {
        LocalDateTime now = LocalDateTime.now();
        return (now.compareTo(order.getTimeToPaid()) >= 0 && !order.isPaid());
    }
}
