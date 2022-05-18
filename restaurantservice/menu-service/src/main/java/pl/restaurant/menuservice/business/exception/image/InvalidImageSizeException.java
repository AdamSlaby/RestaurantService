package pl.restaurant.menuservice.business.exception.image;

public class InvalidImageSizeException extends RuntimeException {
    public InvalidImageSizeException() {
        super("Rozmiar obrazka dla wiadomości jest zbyt duży");
    }
}
