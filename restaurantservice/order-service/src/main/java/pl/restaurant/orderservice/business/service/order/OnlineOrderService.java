package pl.restaurant.orderservice.business.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OnlineOrderInfo;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.business.service.statistic.Time;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    BigDecimal getTodayIncome(Long restaurantId, LocalDateTime from, LocalDateTime to);

    Integer getTodayDeliveredOrders(Long restaurantId, LocalDateTime from, LocalDateTime to);

    Integer getTodayDeliveredMealsAmount(Long restaurantId, LocalDateTime from, LocalDateTime to);

    Integer getActiveOrdersAmount(Long restaurantId, LocalDateTime from, LocalDateTime to);

    List<LocalDateTime> getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to);

    Long getCompareOrderChart(Time time, GenerateChartOptions data);

    BigDecimal getCompareOrderIncomeChart(Time time, GenerateChartOptions data);

    List<ChartData> getPaymentMethodAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getDishAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getDishIncomeChart(Time time, GenerateChartOptions data);

    List<ChartData> getOrdersAmountWithDishesAmountChart(Time time, GenerateChartOptions data);

    List<ChartData> getAvgCompletionTimeWithDishesAmountChart(Time time, GenerateChartOptions data);
}
