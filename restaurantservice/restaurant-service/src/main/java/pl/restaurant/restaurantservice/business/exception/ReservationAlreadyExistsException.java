package pl.restaurant.restaurantservice.business.exception;

public class ReservationAlreadyExistsException extends RuntimeException {
    public ReservationAlreadyExistsException() {
        super("W podanym terminie rezerwacja już istnieje");
    }
}
