package pl.restaurant.orderservice.business.service.order;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderListView;
import pl.restaurant.orderservice.api.response.OrdersInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.api.response.chart.OrderType;
import pl.restaurant.orderservice.business.service.statistic.Time;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderListView getOrderList(OrderFilters filters);

    OrdersInfo getOrderInfo(Long orderId, String type);

    List<ActiveOrder> getActiveOrders(Long restaurantId);

    BigDecimal getTodayIncome(Long restaurantId);

    Integer getTodayDeliveredOrders(Long restaurantId);

    Integer getTodayDeliveredMealsAmount(Long restaurantId);

    Integer getActiveOrdersAmount(Long restaurantId);

    List<LocalDateTime> getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to, OrderType type);

    Map<OrderType, Long> getCompareOrderChart(Time time, GenerateChartOptions data);

    Map<OrderType, BigDecimal> getCompareOrderIncomeChart(Time time, GenerateChartOptions data);

    List<ChartData> getPaymentMethodAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getDishAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getDishIncomeChart(Time time, GenerateChartOptions data);

    List<ChartData> getOrdersAmountWithDishesAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getAvgCompletionTimeWithDishesAmountChart(Time time, GenerateChartOptions data);
}
