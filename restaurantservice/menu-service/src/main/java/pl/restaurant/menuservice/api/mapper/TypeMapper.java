package pl.restaurant.menuservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.menuservice.api.response.Type;
import pl.restaurant.menuservice.data.entity.TypeEntity;

@UtilityClass
public class TypeMapper {
    public static Type mapDataToObject(TypeEntity type) {
        return new Type(type.getTypeId(), type.getName());
    }
}
