package pl.restaurant.employeeservice.business.exception;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("Pracownik o podanym identyfikatorze nie istnieje");
    }
}
