package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.OpeningHour;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class OpeningHourMapper {
    public List<OpeningHour> mapDataToObject(Set<pl.restaurant.restaurantservice.data.entity.OpeningHour> hours) {
        return hours.stream()
                .map(el -> new OpeningHour().builder()
                    .hourId(el.getHourId())
                    .weekDayNr(el.getWeekDayNr())
                    .fromHour(el.getFromHour())
                    .toHour(el.getToHour())
                    .build()).collect(Collectors.toList());
    }
}
