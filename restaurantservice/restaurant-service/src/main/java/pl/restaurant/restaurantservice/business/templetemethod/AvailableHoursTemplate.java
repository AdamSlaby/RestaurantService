package pl.restaurant.restaurantservice.business.templetemethod;

import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.ReservationEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AvailableHoursTemplate {
    public List<AvailableHour> getAvailableHours(Set<FoodTableEntity> tables, LocalDateTime dateTime,
                                                 List<ReservationEntity> reservations, int peopleNr) {
        List<AvailableHour> hours = new ArrayList<>();
        for (int i = 10; i <= 20; i += 2) {
            Set<FoodTableEntity> availableTables = new LinkedHashSet<>(tables);
            removeUnavailableTables(reservations, availableTables, i);
            int availableSeats = 0;
            for (FoodTableEntity table : availableTables)
                availableSeats += table.getSeatsNr();
            if (availableSeats < peopleNr)
                continue;
            addAvailableHours(availableTables, hours, i, dateTime, peopleNr);
        }
        return hours;
    }

    private void removeUnavailableTables(List<ReservationEntity> reservations,
                                         Set<FoodTableEntity> availableTables,
                                         int hour) {
        reservations.stream()
                .filter(el -> el.getFromHour().getHour() == hour)
                .collect(Collectors.toList())
                .forEach(reservation -> {
                    reservation.getTables().forEach(reservationTable -> {
                        FoodTableEntity table = reservationTable.getRestaurantTable().getTable();
                        availableTables.remove(table);
                    });
                });
    }

    abstract void addAvailableHours(Set<FoodTableEntity> availableTables, List<AvailableHour> hours, int hour,
                                    LocalDateTime dateTime, int peopleNr);
}
