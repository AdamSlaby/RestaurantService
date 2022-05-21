package pl.restaurant.supplyservice.business.exception;

public class SupplyNotFoundException extends RuntimeException {
    public SupplyNotFoundException() {
        super("Zaopatrzenie o podanym kluczu nie istnieje");
    }
}
