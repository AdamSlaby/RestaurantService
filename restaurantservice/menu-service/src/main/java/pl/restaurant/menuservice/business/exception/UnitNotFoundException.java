package pl.restaurant.menuservice.business.exception;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(int index) {
        super(index + " Jednostka o podanym identyfikatorze nie istnieje");
    }
}
