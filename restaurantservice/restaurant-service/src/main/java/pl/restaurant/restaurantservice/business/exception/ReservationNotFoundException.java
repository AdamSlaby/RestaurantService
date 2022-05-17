package pl.restaurant.restaurantservice.business.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super("Rezerwacja o podanym identyfikatorze nie istnieje");
    }
}
