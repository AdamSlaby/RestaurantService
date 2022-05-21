package pl.restaurant.supplyservice.business.exception;

public class InvalidNipException extends RuntimeException {
    public InvalidNipException(String fieldName) {
        super(fieldName + ".Podany nip jest nieprawidłowy");
    }
}
