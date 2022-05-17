package pl.restaurant.menuservice.business.exception;

public class IncorrectImageException extends RuntimeException {
    public IncorrectImageException() {
        super("Obraz dla wiadomości jest wymagany");
    }
}
