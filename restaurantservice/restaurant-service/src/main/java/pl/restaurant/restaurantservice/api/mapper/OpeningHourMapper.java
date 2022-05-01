package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.OpeningHour;
import pl.restaurant.restaurantservice.data.entity.OpeningHourEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class OpeningHourMapper {
    public List<OpeningHour> mapDataToObject(Set<OpeningHourEntity> hours) {
        return hours.stream()
                .map(el -> new OpeningHour().builder()
                    .hourId(el.getHourId())
                    .weekDayNr(el.getWeekDayNr())
                    .fromHour(el.getFromHour())
                    .toHour(el.getToHour())
                    .build()).collect(Collectors.toList());
    }

    public List<OpeningHourEntity> mapObjectToData(List<OpeningHour> openingHours, RestaurantEntity restaurant) {
        return openingHours.stream()
                .map(el -> new OpeningHourEntity().builder()
                        .restaurant(restaurant)
                        .weekDayNr(el.getWeekDayNr())
                        .fromHour(el.getFromHour())
                        .toHour(el.getToHour())
                        .build())
                .collect(Collectors.toList());
    }
}
