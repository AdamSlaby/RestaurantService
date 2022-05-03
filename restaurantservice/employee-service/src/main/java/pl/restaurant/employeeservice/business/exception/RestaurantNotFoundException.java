package pl.restaurant.employeeservice.business.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Restauracja o podanym identyfikatorze nie istnieje");
    }
}
