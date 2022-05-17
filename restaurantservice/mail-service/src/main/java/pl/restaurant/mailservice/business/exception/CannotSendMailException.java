package pl.restaurant.mailservice.business.exception;

public class CannotSendMailException extends RuntimeException {
    public CannotSendMailException() {
        super("Wystąpił błąd, wiadomość nie może zostać wysłana");
    }
}
