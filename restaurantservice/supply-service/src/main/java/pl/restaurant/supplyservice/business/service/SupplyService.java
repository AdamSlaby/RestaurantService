package pl.restaurant.supplyservice.business.service;

import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.api.request.NewSupply;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

import java.util.List;

public interface SupplyService {
    void addSupplyValue(Long restaurantId, Good good, UnitEntity unit, String fieldName);

    List<SupplyInfo> getAllSupplies(Long restaurantId);

    void updateSupply(SupplyInfo supply);

    SupplyInfo addSupply(NewSupply supply, String authorization);
}
