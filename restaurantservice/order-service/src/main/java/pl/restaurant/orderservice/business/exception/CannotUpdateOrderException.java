package pl.restaurant.orderservice.business.exception;

public class CannotUpdateOrderException extends RuntimeException {
    public CannotUpdateOrderException() {
        super("Nie można zaktualizować zamówienia, ponieważ zostało już ono dostarczone");
    }
}
