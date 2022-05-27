package pl.restaurant.orderservice.business.service.statistic;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.response.chart.Chart;
import pl.restaurant.orderservice.api.response.chart.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StatisticService {
    BigDecimal getTodayIncome(Long restaurantId);

    Integer getTodayDeliveredOrders(Long restaurantId);

    Integer getTodayDeliveredMealsAmount(Long restaurantId);

    Integer getActiveOrdersAmount(Long restaurantId);

    Chart getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to, OrderType type);

    Chart getStatistics(GenerateChartOptions data);
}
