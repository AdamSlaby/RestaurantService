package pl.restaurant.supplyservice.business.exception;

public class UnitMismatchException extends RuntimeException {
    public UnitMismatchException(String fieldName, String message) {
        super(fieldName + "_" + message);
    }
}
