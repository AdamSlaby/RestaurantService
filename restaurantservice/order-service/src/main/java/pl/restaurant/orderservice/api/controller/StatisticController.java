package pl.restaurant.orderservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.api.response.chart.Chart;
import pl.restaurant.orderservice.api.response.chart.OrderType;
import pl.restaurant.orderservice.business.service.statistic.StatisticService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("/statistic")
@AllArgsConstructor
public class StatisticController {
    private StatisticService statisticService;

    @GetMapping("/income")
    public BigDecimal getTodayIncome(@RequestParam(value = "rId", required = false) Long restaurantId) {
        return statisticService.getTodayIncome(restaurantId);
    }

    @GetMapping("/orders")
    public Integer getTodayDeliveredOrdersAmount(@RequestParam(value = "rId", required = false) Long restaurantId) {
        return statisticService.getTodayDeliveredOrders(restaurantId);
    }

    @GetMapping("/meals")
    public Integer getTodayDeliveredMealsAmount(@RequestParam(value = "rId", required = false) Long restaurantId) {
        return statisticService.getTodayDeliveredMealsAmount(restaurantId);
    }

    @GetMapping("/active")
    public Integer getActiveOrdersAmount(@RequestParam(value = "rId", required = false) Long restaurantId) {
        return statisticService.getActiveOrdersAmount(restaurantId);
    }

    @GetMapping("/hours")
    public Chart getOrderAmountFromHours(@RequestParam(value = "rId", required = false) Long restaurantId) {
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        return statisticService.getOrderAmountFromHours(restaurantId, from, to, OrderType.ALL);
    }

    @PostMapping("/")
    public Chart getStatistics(@RequestBody @Valid GenerateChartOptions data) {
        return statisticService.getStatistics(data);
    }
}
