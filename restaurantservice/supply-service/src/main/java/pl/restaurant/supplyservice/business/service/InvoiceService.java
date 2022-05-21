package pl.restaurant.supplyservice.business.service;

import pl.restaurant.supplyservice.api.request.Invoice;
import pl.restaurant.supplyservice.api.request.InvoiceFilters;
import pl.restaurant.supplyservice.api.response.InvoiceListView;
import pl.restaurant.supplyservice.api.response.InvoiceView;

public interface InvoiceService {
    InvoiceView getInvoice(String invoiceNr);

    InvoiceListView getInvoiceList(InvoiceFilters filters);

    void addInvoice(Invoice invoice);

    void updateInvoice(String invoiceNr, Invoice invoice);
}
