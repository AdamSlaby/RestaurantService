package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.data.entity.FoodTable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class TableMapper {
    public List<Table> mapDataToObject(Set<FoodTable> tables) {
        return tables.stream()
                .map(el -> new Table(el.getTableId(), el.getSeatsNr()))
                .collect(Collectors.toList());
    }
}
