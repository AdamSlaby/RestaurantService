package pl.restaurant.orderservice.business.service.strategy;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.business.service.statistic.Time;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class YearTimePeriodStrategy implements TimePeriodStrategy {
    @Override
    public Time calculateTimePeriod(GenerateChartOptions data) {
        Time time = new Time();
        int year = Integer.parseInt(data.getPeriod());
        LocalDateTime dateTime = LocalDateTime.now();
        time.setFrom(dateTime.withYear(year).withMonth(1).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0));
        time.setTo(dateTime.withYear(year).withMonth(12).with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59));
        return time;
    }
}
