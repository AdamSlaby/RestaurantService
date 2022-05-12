package pl.restaurant.newsservice.business.exception;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {
        super("Wiadomość o podanym iddentyfikatorze nie istnieje");
    }
}
