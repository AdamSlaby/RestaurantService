package pl.restaurant.supplyservice.business.exception;

public class InvoiceAlreadyExistsException extends RuntimeException {
    public InvoiceAlreadyExistsException() {
        super("Faktura o podanym numerze już istnieje");
    }
}
