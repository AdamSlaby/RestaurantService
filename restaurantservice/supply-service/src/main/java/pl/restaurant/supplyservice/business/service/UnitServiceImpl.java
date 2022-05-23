package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.business.exception.UnitNotFoundException;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.UnitRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {
    private UnitRepo unitRepo;

    @Override
    public boolean validateUnitIds(List<Integer> unitIds) {
        return unitRepo.existsAllByUnitIdIn(unitIds);
    }

    @Override
    public UnitEntity getUnit(Integer unitId, String fieldName) {
        return unitRepo.findById(unitId)
                .orElseThrow(() -> new UnitNotFoundException(fieldName));
    }

    @Override
    public UnitEntity getUnit(String unit) {
        return unitRepo.findByName(unit)
                .orElseThrow(UnitNotFoundException::new);
    }

    @Override
    public List<Unit> getAllUnits() {
        return unitRepo.getAllUnits();
    }
}
