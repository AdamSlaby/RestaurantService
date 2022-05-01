package pl.restaurant.restaurantservice.business.exception;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException() {
        super("Stół o podanym numerze nie istnieje");
    }
}
