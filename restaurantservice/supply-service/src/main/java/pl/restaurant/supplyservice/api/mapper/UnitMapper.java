package pl.restaurant.supplyservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

@UtilityClass
public class UnitMapper {

    public static UnitEntity mapObjectToData(Unit unit) {
        return new UnitEntity().builder()
                .unitId(unit.getId())
                .name(unit.getName())
                .build();
    }
}
