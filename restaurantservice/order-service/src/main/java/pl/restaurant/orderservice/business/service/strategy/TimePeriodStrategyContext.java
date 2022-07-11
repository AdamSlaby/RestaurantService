package pl.restaurant.orderservice.business.service.strategy;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.business.service.statistic.Time;

public class TimePeriodStrategyContext {
    private TimePeriodStrategy strategy;

    public void setStrategy(TimePeriodStrategy strategy) {
        this.strategy = strategy;
    }

    public Time execute(GenerateChartOptions data) {
        if (strategy != null)
            return strategy.calculateTimePeriod(data);
        else
            throw new RuntimeException("Time period strategy cannot be null");
    }
}
