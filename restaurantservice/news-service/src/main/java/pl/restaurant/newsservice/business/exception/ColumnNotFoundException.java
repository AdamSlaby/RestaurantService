package pl.restaurant.newsservice.business.exception;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException() {
        super("Podana kolumna do sortowania nie istnieje");
    }
}
