package pl.restaurant.orderservice.business.exception;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException() {
        super("Podana kolumna do sortowania nie istnieje");
    }
}
