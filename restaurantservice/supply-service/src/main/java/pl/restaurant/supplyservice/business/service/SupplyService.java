package pl.restaurant.supplyservice.business.service;

import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

public interface SupplyService {
    void addSupplyValue(Long restaurantId, Good good, UnitEntity unit, String fieldName);
}
