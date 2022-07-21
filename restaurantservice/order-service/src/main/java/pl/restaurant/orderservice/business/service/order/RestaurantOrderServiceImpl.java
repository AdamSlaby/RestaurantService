package pl.restaurant.orderservice.business.service.order;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.orderservice.api.mapper.RestaurantOrderMapper;
import pl.restaurant.orderservice.api.request.*;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.RestaurantOrderInfo;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.business.exception.CannotUpdateOrderException;
import pl.restaurant.orderservice.business.exception.InvalidOrderException;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.business.exception.TableNotFoundException;
import pl.restaurant.orderservice.business.model.MealAmount;
import pl.restaurant.orderservice.business.service.authentication.LoginService;
import pl.restaurant.orderservice.business.service.client.MenuServiceClient;
import pl.restaurant.orderservice.business.service.client.RestaurantServiceClient;
import pl.restaurant.orderservice.business.service.statistic.Time;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;
import pl.restaurant.orderservice.data.repository.RestaurantOrderMealRepo;
import pl.restaurant.orderservice.data.repository.RestaurantOrderRepo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantOrderServiceImpl implements RestaurantOrderService {
    public static final String RESTAURANT_TYPE = "Restaurant";
    private static final String BEARER_TOKEN = "Bearer ";
    private RestaurantOrderRepo orderRepo;
    private RestaurantOrderMealRepo mealRepo;
    private MenuServiceClient menuClient;
    private RestaurantServiceClient restaurantClient;
    private ApplicationContext applicationContext;
    private LoginService loginService;

    @Override
    public Page<OrderShortInfo> getOrderList(OrderFilters filters, Pageable pageable) {
        LocalDateTime orderDate = filters.getOrderDate();
        LocalDateTime from = null;
        LocalDateTime to = null;
        if (orderDate != null) {
            from = orderDate.withHour(0).withMinute(0).withSecond(0);
            to = orderDate.withHour(23).withMinute(59).withSecond(59);
        }
        if (filters.getIsCompleted() != null && filters.getIsCompleted()) {
            return orderRepo.getDeliveredOrders(filters.getRestaurantId(), filters.getOrderId(), from, to,
                    filters.getIsCompleted(), pageable);
        } else
            return orderRepo.getNotDeliveredOrders(filters.getRestaurantId(), filters.getOrderId(), from, to,
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
        KeycloakResponse response = loginService.login();
        String error = menuClient.validateOrders(restaurantOrder.getRestaurantId(), restaurantOrder.getOrders(),
                BEARER_TOKEN + response.getAccess_token());
        loginService.logout(response.getRefresh_token());
        if (error != null)
            throw new InvalidOrderException(error);
        getRestaurantOrderServiceProxy().saveOrder(restaurantOrder);
    }

    @Transactional
    public void saveOrder(RestaurantOrder restaurantOrder) {
        RestaurantOrderEntity restaurantOrderEntity = orderRepo
                .save(RestaurantOrderMapper.mapObjectToData(restaurantOrder));
        for (Order order : restaurantOrder.getOrders())
            mealRepo.save(RestaurantOrderMapper.mapOrderToData(order, restaurantOrderEntity));
    }

    @Override
    @Transactional
    public void updateOrder(Long orderId, RestaurantOrder order) {
        RestaurantOrderEntity restaurantOrderEntity = orderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (restaurantOrderEntity.getDeliveryDate() != null)
            throw new CannotUpdateOrderException();
        KeycloakResponse response = loginService.login();
        String error = menuClient.validateOrders(order.getRestaurantId(), order.getOrders(),
                BEARER_TOKEN + response.getAccess_token());
        loginService.logout(response.getRefresh_token());
        if (error != null)
            throw new InvalidOrderException(error);
        getRestaurantOrderServiceProxy().updateOrder(restaurantOrderEntity, order);
    }

    @Transactional
    public void updateOrder(RestaurantOrderEntity restaurantOrderEntity, RestaurantOrder restaurantOrder) {
        restaurantOrderEntity.setPrice(restaurantOrderEntity.getPrice().add(restaurantOrder.getPrice()));
        orderRepo.save(restaurantOrderEntity);
        for (Order order : restaurantOrder.getOrders())
            mealRepo.save(RestaurantOrderMapper.mapOrderToData(order, restaurantOrderEntity));
    }

    @Override
    public void completeOrder(Long orderId) {
        RestaurantOrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
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

    private RestaurantOrderServiceImpl getRestaurantOrderServiceProxy() {
        return applicationContext.getBean(this.getClass());
    }
}
