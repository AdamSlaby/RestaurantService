package pl.restaurant.orderservice.business.service.strategy;

import pl.restaurant.orderservice.api.request.GenerateChartOptions;
import pl.restaurant.orderservice.business.service.statistic.Time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DayTimePeriodStrategy implements TimePeriodStrategy{
    @Override
    public Time calculateTimePeriod(GenerateChartOptions data) {
        Time time = new Time();
        LocalDateTime dateTime = LocalDateTime.parse(data.getPeriod(), DateTimeFormatter.RFC_1123_DATE_TIME);
        time.setFrom(dateTime.withHour(0).withMinute(0).withSecond(0));
        time.setTo(dateTime.withHour(23).withMinute(59).withSecond(59));
        return time;
    }
}
