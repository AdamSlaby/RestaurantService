package pl.restaurant.employeeservice.business.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException() {
        super("Harmonogram o podanym identyfikatorze nie istnieje");
    }
}
