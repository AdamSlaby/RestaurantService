package pl.restaurant.menuservice.business.exception;

public class InvalidImageSizeException extends RuntimeException {
    public InvalidImageSizeException() {
        super("Rozmiar obrazka dla wiadomości jest zbyt duży");
    }
}
