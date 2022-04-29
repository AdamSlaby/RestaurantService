package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.response.Table;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class TableMapper {
    public List<Table> mapDataToObject(Set<pl.restaurant.restaurantservice.data.entity.Table> tables) {
        return tables.stream()
                .map(el -> new Table(el.getTableId(), el.getSeatsNr()))
                .collect(Collectors.toList());
    }
}
