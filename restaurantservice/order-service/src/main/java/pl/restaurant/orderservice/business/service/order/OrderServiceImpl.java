package pl.restaurant.orderservice.business.service.order;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.request.SortDirection;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderListView;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.OrdersInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.api.response.chart.OrderType;
import pl.restaurant.orderservice.business.exception.ColumnNotFoundException;
import pl.restaurant.orderservice.business.service.statistic.Time;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.restaurant.orderservice.business.service.order.OnlineOrderServiceImpl.ONLINE_TYPE;

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

    @Override
    public BigDecimal getTodayIncome(Long restaurantId) {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        BigDecimal value = onlineService.getTodayIncome(restaurantId, from, to);
        return value.add(restaurantService.getTodayIncome(restaurantId, from, to));
    }

    @Override
    public Integer getTodayDeliveredOrders(Long restaurantId) {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        Integer amount = onlineService.getTodayDeliveredOrders(restaurantId, from, to);
        return amount + restaurantService.getTodayDeliveredOrders(restaurantId, from, to);
    }

    @Override
    public Integer getTodayDeliveredMealsAmount(Long restaurantId) {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        Integer amount = onlineService.getTodayDeliveredMealsAmount(restaurantId, from, to);
        return amount + restaurantService.getTodayDeliveredMealsAmount(restaurantId, from, to);
    }

    @Override
    public Integer getActiveOrdersAmount(Long restaurantId) {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        Integer amount = onlineService.getActiveOrdersAmount(restaurantId, from, to);
        return amount + restaurantService.getActiveOrdersAmount(restaurantId, from, to);
    }

    @Override
    public List<LocalDateTime> getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to, OrderType type) {
        if (type == OrderType.ONLINE)
            return onlineService.getOrderAmountFromHours(restaurantId, from, to);
        if (type == OrderType.RESTAURANT)
            return restaurantService.getOrderAmountFromHours(restaurantId, from, to);
        else {
            List<LocalDateTime> orders = onlineService.getOrderAmountFromHours(restaurantId, from, to);
            orders.addAll(restaurantService.getOrderAmountFromHours(restaurantId, from, to));
            return orders;
        }
    }

    @Override
    public Map<OrderType, Long> getCompareOrderChart(Time time, GenerateChartOptions data) {
        Map<OrderType, Long> map = new HashMap<>();
        map.put(OrderType.ONLINE, onlineService.getCompareOrderChart(time, data));
        map.put(OrderType.RESTAURANT, restaurantService.getCompareOrderChart(time, data));
        return map;
    }

    @Override
    public Map<OrderType, BigDecimal> getCompareOrderIncomeChart(Time time, GenerateChartOptions data) {
        Map<OrderType, BigDecimal> map = new HashMap<>();
        map.put(OrderType.ONLINE, onlineService.getCompareOrderIncomeChart(time, data));
        map.put(OrderType.RESTAURANT, restaurantService.getCompareOrderIncomeChart(time, data));
        return map;
    }

    @Override
    public List<ChartData> getPaymentMethodAmountChart(Time time, GenerateChartOptions data) {
        return onlineService.getPaymentMethodAmountChart(time, data);
    }

    @Override
    public List<ChartData> getDishAmountChart(Time time, GenerateChartOptions data) {
        if (data.getOrderType() == OrderType.ONLINE)
            return onlineService.getDishAmountChart(time, data);
        if (data.getOrderType() == OrderType.RESTAURANT)
            return restaurantService.getDishAmountChart(time, data);
        else {
            Map<String, Long> map = onlineService.getDishAmountChart(time, data).stream()
                    .collect(Collectors.toMap(ChartData::getName, ChartData::getValue));
            List<ChartData> list = restaurantService.getDishAmountChart(time, data);
            for (ChartData chartData : list)
                map.put(chartData.getName(), map.get(chartData.getName()) + chartData.getValue());
            return map.entrySet().stream()
                    .map(e -> new ChartData(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ChartData> getDishIncomeChart(Time time, GenerateChartOptions data) {
        if (data.getOrderType() == OrderType.ONLINE)
            return onlineService.getDishIncomeChart(time, data);
        if (data.getOrderType() == OrderType.RESTAURANT)
            return restaurantService.getDishIncomeChart(time, data);
        else {
            Map<String, Long> map = onlineService.getDishIncomeChart(time, data).stream()
                    .collect(Collectors.toMap(ChartData::getName, ChartData::getValue));
            List<ChartData> list = restaurantService.getDishIncomeChart(time, data);
            for (ChartData chartData : list)
                map.put(chartData.getName(), map.get(chartData.getName()) + chartData.getValue());
            return map.entrySet().stream()
                    .map(e -> new ChartData(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ChartData> getOrdersAmountWithDishesAmountChart(Time time, GenerateChartOptions data) {
        if (data.getOrderType() == OrderType.ONLINE)
            return onlineService.getOrdersAmountWithDishesAmountChart(time, data);
        if (data.getOrderType() == OrderType.RESTAURANT)
            return restaurantService.getOrdersAmountWithDishesAmountChart(time, data);
        else {
            Map<String, Long> map = onlineService.getOrdersAmountWithDishesAmountChart(time, data).stream()
                    .collect(Collectors.toMap(ChartData::getName, ChartData::getValue));
            List<ChartData> list = restaurantService.getOrdersAmountWithDishesAmountChart(time, data);
            for (ChartData chartData : list)
                map.put(chartData.getName(), map.get(chartData.getName()) + chartData.getValue());
            return map.entrySet().stream()
                    .map(e -> new ChartData(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ChartData> getAvgCompletionTimeWithDishesAmountChart(Time time, GenerateChartOptions data) {
        if (data.getOrderType() == OrderType.ONLINE)
            return onlineService.getAvgCompletionTimeWithDishesAmountChart(time, data);
        if (data.getOrderType() == OrderType.RESTAURANT)
            return restaurantService.getAvgCompletionTimeWithDishesAmountChart(time, data);
        else {
            Map<String, Long> map = onlineService.getAvgCompletionTimeWithDishesAmountChart(time, data).stream()
                    .collect(Collectors.toMap(ChartData::getName, ChartData::getValue));
            List<ChartData> list = restaurantService.getAvgCompletionTimeWithDishesAmountChart(time, data);
            for (ChartData chartData : list)
                map.put(chartData.getName(), map.get(chartData.getName()) + chartData.getValue());
            return map.entrySet().stream()
                    .map(e -> new ChartData(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }
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
