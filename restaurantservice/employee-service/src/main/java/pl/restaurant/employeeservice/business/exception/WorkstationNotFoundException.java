package pl.restaurant.employeeservice.business.exception;

public class WorkstationNotFoundException extends RuntimeException {
    public WorkstationNotFoundException() {
        super("Stanowisko pracy o podanym identyfikatorze nie istnieje");
    }
}
