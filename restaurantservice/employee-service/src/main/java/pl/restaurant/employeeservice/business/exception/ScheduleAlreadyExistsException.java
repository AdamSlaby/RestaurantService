package pl.restaurant.employeeservice.business.exception;

public class ScheduleAlreadyExistsException extends RuntimeException {
    public ScheduleAlreadyExistsException() {
        super("W wybranej dacie i godzinie już istnieje grafik dla danego pracownika");
    }
}
