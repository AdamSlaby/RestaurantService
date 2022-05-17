package pl.restaurant.restaurantservice.business.templetemethod;

import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AvailableHoursForLargeGroupTemplate extends AvailableHoursTemplate {
    private List<FoodTableEntity> tables;
    private int result;
    private int tablesSize;

    @Override
    void addAvailableHours(Set<FoodTableEntity> availableTables, List<AvailableHour> hours, int hour,
                           LocalDateTime dateTime, int peopleNr) {
        result = Integer.MIN_VALUE;
        tablesSize = Integer.MAX_VALUE;
        calculateBestTables(peopleNr, new ArrayList<>(), availableTables, peopleNr);
        List<Long> tableIds = tables.stream().map(FoodTableEntity::getTableId).collect(Collectors.toList());
        hours.add(new AvailableHour(dateTime.withHour(hour).withMinute(0).withSecond(0).withNano(0), tableIds));
    }

    private void setTables(List<FoodTableEntity> tables, int peopleNr) {
        int suma = 0;
        for (FoodTableEntity table:tables)
            suma += table.getSeatsNr();
        int rest = suma - peopleNr;
        if ((rest == 0 || rest > result) && tablesSize > tables.size()) {
            this.tables = tables;
            this.result = rest;
            tablesSize = tables.size();
        }
    }

    public void calculateBestTables(int seats, List<FoodTableEntity> tables,
                                    Set<FoodTableEntity> availableTables, int peopleNr) {
        if (seats <= 0)
            setTables(tables, peopleNr);
        for (FoodTableEntity table: availableTables) {
            LinkedHashSet<FoodTableEntity> copy = new LinkedHashSet<>(availableTables);
            copy.remove(table);
            List<FoodTableEntity> list = new ArrayList<>(tables);
            list.add(table);
            calculateBestTables(seats - table.getSeatsNr(), list, copy, peopleNr);
        }
    }
}
