package pl.restaurant.supplyservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.api.request.NewSupply;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

@UtilityClass
public class SupplyMapper {
    public static SupplyInfo mapDataToInfo(SupplyEntity supply, UnitEntity unit) {
        return new SupplyInfo().builder()
                .restaurantId(supply.getRestaurantIngredientId().getRestaurantId())
                .ingredientId(supply.getRestaurantIngredientId().getIngredientId())
                .quantity(supply.getQuantity())
                .unitId(unit.getUnitId())
                .build();
    }

    public static SupplyEntity mapObjectToData(NewSupply supply, UnitEntity unit, Integer ingredientId) {
        return new SupplyEntity().builder()
                .restaurantIngredientId(new RestaurantIngredientId(supply.getRestaurantId(), ingredientId))
                .quantity(supply.getQuantity())
                .unit(unit)
                .build();
    }
}
