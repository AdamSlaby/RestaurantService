package pl.restaurant.employeeservice.business.exception;

public class InvalidPeselException extends RuntimeException {
    public InvalidPeselException() {
        super("Podany pesel jest nieprawidłowy");
    }
}
