package pl.restaurant.orderservice.business.service.statistic;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Time {
    LocalDateTime from;
    LocalDateTime to;
}
