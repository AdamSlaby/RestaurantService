package pl.restaurant.menuservice.business.exception;

public class SupplyErrorException extends RuntimeException {
    public SupplyErrorException(String message) {
        super(message);
    }
}
