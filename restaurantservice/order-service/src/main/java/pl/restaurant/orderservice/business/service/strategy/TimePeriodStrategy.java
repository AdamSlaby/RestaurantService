package pl.restaurant.orderservice.business.service.strategy;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.business.service.statistic.Time;

public interface TimePeriodStrategy {
    Time calculateTimePeriod(GenerateChartOptions data);
}
