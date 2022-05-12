package pl.restaurant.newsservice.business.exception;

public class IncorrectImageExtensionException extends RuntimeException {
    public IncorrectImageExtensionException() {
        super("Obrazek o podanym rozszerzeniu jest nieobsługiwany. Dostępne rozszerzenia to jpg lub jpeg");
    }
}
