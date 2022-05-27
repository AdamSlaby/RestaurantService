package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.supplyservice.api.mapper.SupplyMapper;
import pl.restaurant.supplyservice.api.mapper.UnitMapper;
import pl.restaurant.supplyservice.api.request.*;
import pl.restaurant.supplyservice.business.calculator.CalculateUnitCreator;
import pl.restaurant.supplyservice.business.calculator.Creator;
import pl.restaurant.supplyservice.business.calculator.Result;
import pl.restaurant.supplyservice.business.calculator.UnitCalculator;
import pl.restaurant.supplyservice.business.exception.*;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.SupplyRepo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public void updateSupplies(Long restaurantId, List<OrderValidation> orders) {
        Map<Integer, SupplyEntity> supplies = getSuppliesMapIn(restaurantId, orders);
        for (OrderValidation order : orders) {
            checkSupplies(supplies, order, true);
        }
    }

    @Override
    public void checkSupplies(Long restaurantId, OrderValidation order) {
        List<RestaurantIngredientId> ids = order.getIngredients().stream()
                .map(ingredient -> new RestaurantIngredientId(restaurantId, ingredient.getId()))
                .collect(Collectors.toList());
        Map<Integer, SupplyEntity> supplies = supplyRepo.getAllByRestaurantIngredientIdIn(ids).stream()
                .collect(Collectors.toMap(k -> k.getRestaurantIngredientId().getIngredientId(), v -> v));
        checkSupplies(supplies, order, false);
    }

    @Override
    @Transactional
    public void rollbackOrderSupplies(Long restaurantId, List<OrderValidation> orders) {
        Map<Integer, SupplyEntity> supplies = getSuppliesMapIn(restaurantId, orders);
        for (OrderValidation order : orders) {
            for (IngredientView ingredient : order.getIngredients()) {
                SupplyEntity supply = supplies.get(ingredient.getId());
                Creator<UnitCalculator, UnitEntity> creator = new CalculateUnitCreator();
                UnitEntity unit = UnitMapper.mapObjectToData(ingredient.getUnit());
                BigDecimal quantity = ingredient.getAmount().multiply(BigDecimal.valueOf(order.getAmount()));
                try {
                    UnitCalculator unitCalculator = creator.create(supply.getUnit(), unit);
                    Result result = unitCalculator.calculateUnits(supply, quantity, unit, Operation.ADD);
                    supply.setQuantity(result.getResult());
                    supply.setUnit(unitService.getUnit(result.getUnit()));
                    supplyRepo.save(supply);
                } catch (RuntimeException ex) {
                    throw new SupplyErrorException(ex.getMessage());
                }
            }
        }
    }

    public void checkSupplies(Map<Integer, SupplyEntity> supplies, OrderValidation order, boolean update) {
        for (IngredientView ingredient : order.getIngredients()) {
            SupplyEntity supply = supplies.get(ingredient.getId());
            Creator<UnitCalculator, UnitEntity> creator = new CalculateUnitCreator();
            UnitEntity unit = UnitMapper.mapObjectToData(ingredient.getUnit());
            BigDecimal quantity = ingredient.getAmount().multiply(BigDecimal.valueOf(order.getAmount()));
            try {
                UnitCalculator unitCalculator = creator.create(supply.getUnit(), unit);
                Result result = unitCalculator.calculateUnits(supply, quantity, unit, Operation.SUBTRACT);
                if (result.getResult().compareTo(BigDecimal.ZERO) < 0)
                    throw new RuntimeException(order.getName() + " skończył się. Proszę wybrać inny posiłek");
                if (update) {
                    supply.setQuantity(result.getResult());
                    supply.setUnit(unitService.getUnit(result.getUnit()));
                    supplyRepo.save(supply);
                }
            } catch (RuntimeException ex) {
                throw new SupplyErrorException(ex.getMessage());
            }
        }
    }

    private Map<Integer, SupplyEntity> getSuppliesMapIn(Long restaurantId, List<OrderValidation> orders) {
        List<RestaurantIngredientId> ids = new ArrayList<>();
        orders.forEach(order -> ids.addAll(order.getIngredients().stream()
                .map(ingredient -> new RestaurantIngredientId(restaurantId, ingredient.getId()))
                .collect(Collectors.toList())));
        return supplyRepo.getAllByRestaurantIngredientIdIn(ids).stream()
                .collect(Collectors.toMap(k -> k.getRestaurantIngredientId().getIngredientId(), v -> v));
    }
}
