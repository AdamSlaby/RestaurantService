package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Service;
import pl.restaurant.supplyservice.api.mapper.SupplyMapper;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.api.request.NewSupply;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.business.calculator.CalculateUnitCreator;
import pl.restaurant.supplyservice.business.calculator.Creator;
import pl.restaurant.supplyservice.business.calculator.Result;
import pl.restaurant.supplyservice.business.calculator.UnitCalculator;
import pl.restaurant.supplyservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.supplyservice.business.exception.SupplyAlreadyExistsException;
import pl.restaurant.supplyservice.business.exception.SupplyNotFoundException;
import pl.restaurant.supplyservice.business.exception.UnitMismatchException;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.SupplyRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class SupplyServiceImpl implements SupplyService {
    private SupplyRepo supplyRepo;
    private UnitService unitService;
    private MenuServiceClient menuClientService;
    private RestaurantServiceClient restaurantService;

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

    @Override
    public List<SupplyInfo> getAllSupplies(Long restaurantId) {
        return supplyRepo.getAllSupplies(restaurantId);
    }

    @Override
    @Transactional
    public void updateSupply(SupplyInfo supply) {
        SupplyEntity supplyEntity = supplyRepo.findById(new RestaurantIngredientId(supply.getRestaurantId(),
                        supply.getIngredientId()))
                .orElseThrow(SupplyNotFoundException::new);
        UnitEntity unit = unitService
                .getUnit(supply.getUnitId(), "unitId");
        supplyEntity.setQuantity(supply.getQuantity());
        supplyEntity.setUnit(unit);
        supplyRepo.save(supplyEntity);
    }

    @Override
    public SupplyInfo addSupply(NewSupply supply, String authorization) {
        Integer ingredientId = menuClientService
                .getIngredient(supply.getIngredientName(), authorization);
        if (!restaurantService.isRestaurantExist(supply.getRestaurantId()))
            throw new RestaurantNotFoundException();
        if (supplyRepo.existsById(new RestaurantIngredientId(supply.getRestaurantId(), ingredientId)))
            throw new SupplyAlreadyExistsException();
        UnitEntity unit = unitService.getUnit(supply.getUnitId(), "unitId");
        return SupplyMapper.mapDataToInfo(supplyRepo.save(SupplyMapper.mapObjectToData(supply, unit, ingredientId)), unit);
    }
}
