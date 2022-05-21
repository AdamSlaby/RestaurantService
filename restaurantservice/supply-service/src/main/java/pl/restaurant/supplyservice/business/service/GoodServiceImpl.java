package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.supplyservice.api.mapper.GoodMapper;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.GoodRepo;

@Service
@AllArgsConstructor
public class GoodServiceImpl implements GoodService {
    private GoodRepo goodRepo;
    private UnitService unitService;
    private SupplyService supplyService;

    @Override
    public void addGood(Good good, InvoiceEntity invoice, String fieldName, boolean updateSupply) {
        UnitEntity unit = unitService.getUnit(good.getUnitId(), fieldName + "unitId");
        goodRepo.save(GoodMapper.mapObjectToData(good, invoice, unit));
        if (updateSupply)
            supplyService.addSupplyValue(invoice.getRestaurantId(), good, unit, fieldName + "unitId");
    }

    @Override
    @Transactional
    public void deleteAllByInvoice(InvoiceEntity invoice) {
        goodRepo.deleteAllByInvoice(invoice.getInvoiceNr());
    }
}
