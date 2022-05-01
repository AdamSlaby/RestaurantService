package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class TableMapper {
    public List<Table> mapDataToObject(Set<FoodTableEntity> tables) {
        return tables.stream()
                .map(el -> new Table(el.getTableId(), el.getSeatsNr()))
                .collect(Collectors.toList());
    }

    public Table mapDataToObject(FoodTableEntity table) {
        return new Table(table.getTableId(), table.getSeatsNr());
    }
}
