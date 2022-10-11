package pl.restaurant.orderservice.business.service.order;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.orderservice.api.mapper.AddressMapper;
import pl.restaurant.orderservice.api.mapper.OnlineOrderMapper;
import pl.restaurant.orderservice.api.request.*;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OnlineOrderInfo;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.business.exception.CannotCompleteOrderException;
import pl.restaurant.orderservice.business.exception.InvalidOrderException;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.orderservice.business.model.MealAmount;
import pl.restaurant.orderservice.business.service.authentication.LoginService;
import pl.restaurant.orderservice.business.service.client.MenuServiceClient;
import pl.restaurant.orderservice.business.service.client.RestaurantServiceClient;
import pl.restaurant.orderservice.business.service.statistic.Time;
import pl.restaurant.orderservice.data.entity.AddressEntity;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;
import pl.restaurant.orderservice.data.entity.PaymentMethod;
import pl.restaurant.orderservice.data.repository.AddressRepo;
import pl.restaurant.orderservice.data.repository.OnlineOrderMealRepo;
import pl.restaurant.orderservice.data.repository.OnlineOrderRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {
    public static final String ONLINE_TYPE = "Online";
    private static final String BEARER_TOKEN = "Bearer ";
    public static final int TIME_TO_PAID = 30;
    private OnlineOrderRepo orderRepo;
    private AddressRepo addressRepo;
    private OnlineOrderMealRepo mealRepo;
    private MenuServiceClient menuClient;
    private RestaurantServiceClient restaurantClient;
    private RabbitTemplate rabbitTemplate;
    private ApplicationContext applicationContext;
    private LoginService loginService;

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
        if (filters.getIsCompleted() != null && filters.getIsCompleted())
            return orderRepo.getDeliveredOrders(filters.getRestaurantId(), filters.getOrderId(), from, to,
                    filters.getIsCompleted(), pageable);
        else
            return orderRepo.getNotDeliveredOrders(filters.getRestaurantId(), filters.getOrderId(), from, to,
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
        KeycloakResponse response = loginService.login();
        String error = menuClient.validateOrders(onlineOrder.getRestaurantId(), onlineOrder.getOrders(),
                BEARER_TOKEN + response.getAccess_token());
        loginService.logout(response.getRefresh_token());
        if (error != null)
            throw new InvalidOrderException(error);
        return getOnlineOrderServiceProxy().saveOrder(onlineOrder);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long saveOrder(OnlineOrder onlineOrder) {
        try {
            BigDecimal fee = restaurantClient.getRestaurantDeliveryFee(onlineOrder.getRestaurantId());
            BigDecimal price = BigDecimal.ZERO;
            for (Order order : onlineOrder.getOrders())
                price = price.add(order.getPrice());
            price = price.add(fee == null ? BigDecimal.ZERO : fee);
            AddressEntity address = addressRepo.save(AddressMapper.mapObjectToData(onlineOrder.getAddress()));
            OnlineOrderEntity onlineOrderEntity = orderRepo
                    .save(OnlineOrderMapper.mapObjectToData(onlineOrder, price, address));
            Set<OnlineOrderMealEntity> meals = new HashSet<>();
            for (Order order : onlineOrder.getOrders())
                meals.add(mealRepo.save(OnlineOrderMapper.mapOrderToData(order, onlineOrderEntity)));
            if (onlineOrder.getPaymentMethod() != PaymentMethod.PAYPAL &&
                    onlineOrder.getPaymentMethod() != PaymentMethod.PAYU) {
                RestaurantShortInfo restaurant = restaurantClient.getRestaurantShortInfo(onlineOrder.getRestaurantId());
                rabbitTemplate.convertAndSend("order",
                        OnlineOrderMapper.mapDataToEmailInfo(onlineOrderEntity, meals, restaurant));
            }
            return onlineOrderEntity.getOrderId();
        } catch (Exception ex) {
            KeycloakResponse response = loginService.login();
            menuClient.rollbackOrderSupplies(onlineOrder.getRestaurantId(), onlineOrder.getOrders(),
                    BEARER_TOKEN + response.getAccess_token());
            loginService.logout(response.getRefresh_token());
            throw ex;
        }
    }

    public void rollbackOrder(Long orderId) {
        OnlineOrderEntity order = orderRepo.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        getOnlineOrderServiceProxy().rollbackOrder(order);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void rollbackOrder(OnlineOrderEntity order) {
        List<Order> orders = order.getMeals().stream()
                .map(OnlineOrderMapper::mapDataToOrder)
                .collect(Collectors.toList());
        KeycloakResponse response = loginService.login();
        menuClient.rollbackOrderSupplies(order.getRestaurantId(), orders,
                BEARER_TOKEN + response.getAccess_token());
        loginService.logout(response.getRefresh_token());
        deleteOrder(order);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOrder(OnlineOrderEntity order) {
        orderRepo.deleteById(order.getOrderId());
        mealRepo.deleteMealsByOrderId(order.getOrderId());
    }

    @Override
    public void changePaymentStatus(Long orderId) {
        OnlineOrderEntity order = orderRepo.getByOrderId(orderId).orElseThrow(OrderNotFoundException::new);
        order.setPaid(true);
        orderRepo.save(order);
        RestaurantShortInfo restaurant = restaurantClient.getRestaurantShortInfo(order.getRestaurantId());
        rabbitTemplate.convertAndSend("order", OnlineOrderMapper.mapDataToEmailInfo(order, restaurant));
    }

    @Override
    public void completeOrder(Long orderId) {
        OnlineOrderEntity order = orderRepo.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (!order.isPaid() && (order.getPaymentMethod() == PaymentMethod.PAYPAL ||
                order.getPaymentMethod() == PaymentMethod.PAYU))
            throw new CannotCompleteOrderException();
        else
            order.setPaid(true);
        order.setDeliveryDate(LocalDateTime.now());
        orderRepo.save(order);
    }

    @Override
    public BigDecimal getTodayIncome(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        BigDecimal income = orderRepo.getTodayIncome(restaurantId, from, to);
        return income == null ? BigDecimal.ZERO : income;
    }

    @Override
    public Integer getTodayDeliveredOrders(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        Integer amount = orderRepo.getTodayDeliveredOrders(restaurantId, from, to);
        return amount == null ? 0 : amount;
    }

    @Override
    public Integer getTodayDeliveredMealsAmount(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        Integer amount = mealRepo.getTodayDeliveredMealsAmount(restaurantId, from, to);
        return amount == null ? 0 : amount;
    }

    @Override
    public Integer getActiveOrdersAmount(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        Integer amount = orderRepo.getActiveOrdersAmount(restaurantId, from, to);
        return amount == null ? 0 : amount;
    }

    @Override
    public List<LocalDateTime> getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        return orderRepo.getOrderAmountFromHours(restaurantId, from, to);
    }

    @Override
    public Long getCompareOrderChart(Time time, GenerateChartOptions data) {
        return orderRepo.getCompareOrderChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public BigDecimal getCompareOrderIncomeChart(Time time, GenerateChartOptions data) {
        BigDecimal income = orderRepo.getCompareOrderIncomeChart(data.getPlaceId(), time.getFrom(), time.getTo());
        return income == null ? BigDecimal.ZERO : income;
    }

    @Override
    public List<ChartData> getPaymentMethodAmountChart(Time time, GenerateChartOptions data) {
        return orderRepo.getPaymentMethodAmountChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public List<ChartData> getDishAmountChart(Time time, GenerateChartOptions data) {
        return mealRepo.getDishAmountChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public List<ChartData> getDishIncomeChart(Time time, GenerateChartOptions data) {
        return mealRepo.getDishIncomeChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public List<ChartData> getOrdersAmountWithDishesAmountChart(Time time, GenerateChartOptions data) {
        return orderRepo.getOrdersAmountWithDishesAmountChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public List<ChartData> getAvgCompletionTimeWithDishesAmountChart(Time time, GenerateChartOptions data) {
        return orderRepo.getAvgCompletionTimeWithDishesAmountChart(data.getPlaceId(), time.getFrom(), time.getTo());
    }

    @Override
    public Map<Integer, MealAmount> getMostPopularMeals(Time time) {
        return mealRepo.getMealsAmount(time.getFrom(), time.getTo()).stream()
                .collect(Collectors.toMap(MealAmount::getMealId, v -> v));
    }

    //method will be invoked every 30 minutes (1000 milis * 60 sec * 30 min = 1800000 milis)
    @Scheduled(fixedRate = 1800000)
    public void checkIfOrderWasPaid() {
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<OnlineOrderEntity> orders = orderRepo.getOrdersByDate(from, to);
        for (OnlineOrderEntity order : orders) {
            if (isOrderUnpaid(order))
                getOnlineOrderServiceProxy().rollbackOrder(order);
        }
    }

    private OnlineOrderServiceImpl getOnlineOrderServiceProxy() {
        return applicationContext.getBean(this.getClass());
    }

    private boolean isOrderUnpaid(OnlineOrderEntity order) {
        LocalDateTime now = LocalDateTime.now();
        if (order.getTimeToPaid() != null)
            return (now.compareTo(order.getTimeToPaid()) >= 0 && !order.isPaid());
        else
            return false;
    }
}
