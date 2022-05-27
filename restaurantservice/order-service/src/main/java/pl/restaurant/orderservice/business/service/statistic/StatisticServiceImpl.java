package pl.restaurant.orderservice.business.service.statistic;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.response.chart.Chart;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.api.response.chart.OrderType;
import pl.restaurant.orderservice.api.response.chart.PeriodType;
import pl.restaurant.orderservice.business.service.order.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private OrderService orderService;

    @Override
    public BigDecimal getTodayIncome(Long restaurantId) {
        return orderService.getTodayIncome(restaurantId);
    }

    @Override
    public Integer getTodayDeliveredOrders(Long restaurantId) {
        return orderService.getTodayDeliveredOrders(restaurantId);
    }

    @Override
    public Integer getTodayDeliveredMealsAmount(Long restaurantId) {
        return orderService.getTodayDeliveredMealsAmount(restaurantId);
    }

    @Override
    public Integer getActiveOrdersAmount(Long restaurantId) {
        return orderService.getActiveOrdersAmount(restaurantId);
    }

    @Override
    public Chart getOrderAmountFromHours(Long restaurantId, LocalDateTime from, LocalDateTime to, OrderType type) {
        List<LocalDateTime> orders = orderService.getOrderAmountFromHours(restaurantId, from, to, type);
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 8; i <= 20; i++)
            map.put(i, 0);
        for (LocalDateTime time : orders)
            map.put(time.getHour(), map.get(time.getHour()) + 1);
        return new Chart().builder()
                .Xlabel("Godzina")
                .Ylabel("Liczba zamówień")
                .name("Liczba zamówień w zależności od godziny")
                .series(map.entrySet().stream()
                        .map(e -> new ChartData(e.getKey() + ":00", Long.valueOf(e.getValue())))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Chart getStatistics(GenerateChartOptions data) {
        Time time = setTime(data);
        switch (data.getChartName()) {
            case COMPARE_ORDERS_AMOUNT:
                return getCompareOrderChart(time, data);
            case COMPARE_ORDERS_INCOME:
                return getCompareOrderIncomeChart(time, data);
            case ORDERS_AMOUNT_USING_DIFFERENT_PAYMENT_METHODS:
                return getPaymentMethodAmountChart(time, data);
            case ORDERS_AMOUNT_IN_DIFFERENT_HOURS:
                return getOrdersInHoursChart(time, data);
            case SOLD_DISH_AMOUNT:
                return getDishAmountChart(time, data);
            case SOLD_DISH_INCOME:
                return getDishIncomeChart(time, data);
            case AVERAGE_COMPLETION_ORDER_TIME_DEPENDS_ON_DISHES_AMOUNT:
                return getAvgCompletionTimeWithDishesAmountChart(time, data);
            case ORDERS_AMOUNT_CONSIDERING_DISHES_AMOUNT_IN_EACH:
                return getOrdersAmountWithDishesAmountChart(time, data);
            default:
                throw new RuntimeException("Invalid chart name");
        }
    }

    private Chart getAvgCompletionTimeWithDishesAmountChart(Time time, GenerateChartOptions data) {
        List<ChartData> list = orderService.getAvgCompletionTimeWithDishesAmountChart(time, data);
        return new Chart().builder()
                .Xlabel("Liczba potraw z zamówieniu")
                .Ylabel("Średni czas dostawy potaw w minutach")
                .name("Średni czas dostaw zamówień w zależności od liczby posiłków")
                .series(list)
                .build();
    }

    private Chart getOrdersAmountWithDishesAmountChart(Time time, GenerateChartOptions data) {
        List<ChartData> list = orderService.getOrdersAmountWithDishesAmountChart(time, data);
        return new Chart().builder()
                .Xlabel("Liczba potraw w zamówieniu")
                .Ylabel("Liczba takich zamówień")
                .name("Liczba zamówień pod względem ilości potraw w nich")
                .series(list)
                .build();
    }

    private Chart getDishIncomeChart(Time time, GenerateChartOptions data) {
        List<ChartData> list = orderService.getDishIncomeChart(time, data);
        return new Chart().builder()
                .Xlabel("Potrawa")
                .Ylabel("Dochód ze sprzedanych potraw")
                .name("Dochód ze sprzedanych poszczególnych potraw")
                .series(list)
                .build();
    }

    private Chart getDishAmountChart(Time time, GenerateChartOptions data) {
        List<ChartData> list = orderService.getDishAmountChart(time, data);
        return new Chart().builder()
                .Xlabel("Potrawa")
                .Ylabel("Liczba sprzedanych potraw")
                .name("Liczba sprzedanych poszczególnych potraw")
                .series(list)
                .build();
    }

    private Chart getOrdersInHoursChart(Time time, GenerateChartOptions data) {
        return getOrderAmountFromHours(data.getPlaceId(), time.getFrom(), time.getTo(), data.getOrderType());
    }

    private Chart getPaymentMethodAmountChart(Time time, GenerateChartOptions data) {
        List<ChartData> list = orderService.getPaymentMethodAmountChart(time, data);
        return new Chart().builder()
                .Xlabel("Typ płatności")
                .Ylabel("Liczba zamówień")
                .name("Liczba zamówień z użyciem różnych metod płatności")
                .series(list)
                .build();
    }

    private Chart getCompareOrderIncomeChart(Time time, GenerateChartOptions data) {
        Map<OrderType, BigDecimal> map = orderService.getCompareOrderIncomeChart(time, data);
        return new Chart().builder()
                .Xlabel("Typ zamówienia")
                .Ylabel("Dochód z zamówień")
                .name("Przychód z zamówień online w stosunku do liczby zamówień na miejscu")
                .series(map.entrySet().stream()
                        .map(e -> new ChartData(e.getKey().toString(), e.getValue().longValue()))
                        .collect(Collectors.toList()))
                .build();
    }

    private Chart getCompareOrderChart(Time time, GenerateChartOptions data) {
        Map<OrderType, Long> map = orderService.getCompareOrderChart(time, data);
        return new Chart().builder()
                .Xlabel("Typ zamówienia")
                .Ylabel("Liczba zamówień")
                .name("Liczba zamówień online w stosunku do liczby zamówień na miejscu")
                .series(map.entrySet().stream()
                        .map(e -> new ChartData(e.getKey().toString(), e.getValue()))
                        .collect(Collectors.toList()))
                .build();
    }

    private Time setTime(GenerateChartOptions data) {
        Time time = new Time();
        if (data.getPeriodType() == PeriodType.DAY) {
            LocalDateTime dateTime = LocalDateTime.parse(data.getPeriod());
            time.setFrom(dateTime.withHour(0).withMinute(0).withSecond(0));
            time.setTo(dateTime.withHour(23).withMinute(59).withSecond(59));
            return time;
        } if (data.getPeriodType() == PeriodType.MONTH) {
            int month = Integer.parseInt(data.getPeriod());
            LocalDateTime dateTime = LocalDateTime.now();
            time.setFrom(dateTime.withMonth(month).withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0));
            time.setTo(dateTime.withMonth(month).with(TemporalAdjusters.lastDayOfMonth())
                    .withHour(23).withMinute(59).withSecond(59));
            return time;
        } if (data.getPeriodType() == PeriodType.YEAR) {
            int year = Integer.parseInt(data.getPeriod());
            LocalDateTime dateTime = LocalDateTime.now();
            time.setFrom(dateTime.withYear(year).withMonth(1).withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0));
            time.setTo(dateTime.withYear(year).withMonth(12).with(TemporalAdjusters.lastDayOfMonth())
                    .withHour(23).withMinute(59).withSecond(59));
            return time;
        }
        throw new RuntimeException("Period type not found");
    }
}
