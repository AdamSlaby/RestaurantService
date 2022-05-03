package pl.restaurant.employeeservice.business.exception;

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException() {
        super("Podany numer konta bankowego jest nieprawidłowy");
    }
}
