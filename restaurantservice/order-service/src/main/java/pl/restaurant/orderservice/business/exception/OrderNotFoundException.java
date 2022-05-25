package pl.restaurant.orderservice.business.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Zamówienie o podanym identyfikatorze nie istnieje");
    }
}
