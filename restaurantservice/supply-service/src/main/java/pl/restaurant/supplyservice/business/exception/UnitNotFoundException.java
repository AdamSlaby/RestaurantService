package pl.restaurant.supplyservice.business.exception;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException(String fieldName) {
        super(fieldName + "_Jednostka o podanym identyfikatorze nie istnieje");
    }

    public UnitNotFoundException() {
        super("Jednostka o podanej nazwie nie istnieje");
    }
}
