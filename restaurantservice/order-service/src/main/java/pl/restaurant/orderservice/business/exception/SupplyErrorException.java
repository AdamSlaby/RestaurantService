package pl.restaurant.orderservice.business.exception;

public class SupplyErrorException extends RuntimeException {
    public SupplyErrorException(String message) {
        super(message);
    }
}
