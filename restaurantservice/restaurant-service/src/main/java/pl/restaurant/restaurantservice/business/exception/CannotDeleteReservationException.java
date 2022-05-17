package pl.restaurant.restaurantservice.business.exception;

public class CannotDeleteReservationException extends RuntimeException {
    public CannotDeleteReservationException() {
        super("Rezerwacja nie może zostać usunięta.");
    }
}
