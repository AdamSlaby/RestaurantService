package pl.restaurant.employeeservice.business.exception;

public class InvalidLoginCredentialsException extends RuntimeException {
    public InvalidLoginCredentialsException() {
        super("Hasło lub nazwa użytkownika są nieprawidłowe");
    }
}
