package pl.restaurant.newsservice.business.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("Pracownik o podanym identyfikatorze nie istnieje");
    }
}
