package pl.restaurant.supplyservice.business.service;

import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;

public interface GoodService {
    void addGood(Good good, InvoiceEntity invoice, String fieldName, boolean updateSupply);

    void deleteAllByInvoice(InvoiceEntity invoice);
}
