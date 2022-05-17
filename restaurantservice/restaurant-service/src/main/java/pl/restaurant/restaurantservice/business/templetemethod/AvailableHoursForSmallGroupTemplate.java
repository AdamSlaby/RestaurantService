package pl.restaurant.restaurantservice.business.templetemethod;

import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class AvailableHoursForSmallGroupTemplate extends AvailableHoursTemplate {
    @Override
    void addAvailableHours(Set<FoodTableEntity> availableTables, List<AvailableHour> hours, int hour,
                           LocalDateTime dateTime, int peopleNr) {
        for (FoodTableEntity table : availableTables) {
            if (table.getSeatsNr() - peopleNr >= 0) {
                hours.add(new AvailableHour(dateTime.withHour(hour).withMinute(0).withSecond(0).withNano(0)
                        , List.of(table.getTableId())));
                break;
            }
        }
    }
}
