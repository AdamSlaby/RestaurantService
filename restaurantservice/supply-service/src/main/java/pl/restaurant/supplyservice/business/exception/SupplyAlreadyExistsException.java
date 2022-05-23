package pl.restaurant.supplyservice.business.exception;

public class SupplyAlreadyExistsException extends RuntimeException {
    public SupplyAlreadyExistsException() {
        super("Zaopatrzenie dla tego składnika już istnieje");
    }
}
