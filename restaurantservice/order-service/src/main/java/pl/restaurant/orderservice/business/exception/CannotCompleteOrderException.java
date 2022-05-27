package pl.restaurant.orderservice.business.exception;

public class CannotCompleteOrderException extends RuntimeException {
    public CannotCompleteOrderException() {
        super("Zamówienie nie może zostać zrealizowane, ponieważ nie została uiszczona opłata za nie");
    }
}
