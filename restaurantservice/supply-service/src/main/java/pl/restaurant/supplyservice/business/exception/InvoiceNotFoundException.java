package pl.restaurant.supplyservice.business.exception;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException() {
        super("Faktura o podanym numerze nie istnieje");
    }
}
