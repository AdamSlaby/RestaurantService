package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.response.Unit;
import pl.restaurant.menuservice.business.exception.UnitNotFoundException;
import pl.restaurant.menuservice.data.entity.UnitEntity;
import pl.restaurant.menuservice.data.repository.UnitRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {
    private UnitRepo unitRepo;
    @Override
    public List<Unit> getAllUnits() {
        return unitRepo.getAllUnits();
    }

    @Override
    public UnitEntity getUnit(Integer unitId, int index) {
        return unitRepo.findById(unitId)
                .orElseThrow(() -> new UnitNotFoundException(index));
    }
}
