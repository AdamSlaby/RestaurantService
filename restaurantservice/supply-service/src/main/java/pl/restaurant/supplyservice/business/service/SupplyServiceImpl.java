package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Service;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.business.calculator.CalculateUnitCreator;
import pl.restaurant.supplyservice.business.calculator.Creator;
import pl.restaurant.supplyservice.business.calculator.Result;
import pl.restaurant.supplyservice.business.calculator.UnitCalculator;
import pl.restaurant.supplyservice.business.exception.SupplyNotFoundException;
import pl.restaurant.supplyservice.business.exception.UnitMismatchException;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.SupplyRepo;

@Service
@AllArgsConstructor
public class SupplyServiceImpl implements SupplyService {
    private SupplyRepo supplyRepo;
    private UnitService unitService;

    @Override
    public void addSupplyValue(Long restaurantId, Good good, UnitEntity unit, String fieldName) {
        SupplyEntity supply = supplyRepo
                .getByRestaurantIngredientId(new RestaurantIngredientId(restaurantId, good.getIngredientId()))
                .orElseThrow(SupplyNotFoundException::new);
        Creator<UnitCalculator, UnitEntity> creator = new CalculateUnitCreator();
        try {
            UnitCalculator unitCalculator = creator.create(supply.getUnit(), unit);
            Result result = unitCalculator.calculateUnits(supply, good.getQuantity(), unit, Operation.ADD);
            supply.setQuantity(result.getResult());
            supply.setUnit(unitService.getUnit(result.getUnit()));
            supplyRepo.save(supply);
        } catch (RuntimeException ex) {
            throw new UnitMismatchException(fieldName, ex.getMessage());
        }
    }
}
