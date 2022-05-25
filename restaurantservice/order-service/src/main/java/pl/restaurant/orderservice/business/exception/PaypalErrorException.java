package pl.restaurant.orderservice.business.exception;

public class PaypalErrorException extends RuntimeException {
    public PaypalErrorException(String message) {
        super(message);
    }
}
