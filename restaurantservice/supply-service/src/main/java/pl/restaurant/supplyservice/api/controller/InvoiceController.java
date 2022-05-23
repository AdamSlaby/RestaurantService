package pl.restaurant.supplyservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.supplyservice.api.request.Invoice;
import pl.restaurant.supplyservice.api.request.InvoiceFilters;
import pl.restaurant.supplyservice.api.response.InvoiceListView;
import pl.restaurant.supplyservice.api.response.InvoiceView;
import pl.restaurant.supplyservice.business.service.InvoiceService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/invoice")
@AllArgsConstructor
public class InvoiceController {
    private InvoiceService invoiceService;

    @GetMapping("/")
    public InvoiceView getInvoice(@RequestParam("nr") String invoiceNr) {
        return invoiceService.getInvoice(invoiceNr);
    }

    @PostMapping("/list")
    public InvoiceListView getInvoiceList(@RequestBody @Valid InvoiceFilters filters) {
        return invoiceService.getInvoiceList(filters);
    }

    @PostMapping("/")
    public void addInvoice(@RequestBody @Valid Invoice invoice) {
        invoiceService.addInvoice(invoice);
    }

    @PutMapping("/")
    public void updateInvoice(@RequestBody @Valid Invoice invoice, @RequestParam("nr") String invoiceNr) {
        invoiceService.updateInvoice(invoiceNr, invoice);
    }
}
