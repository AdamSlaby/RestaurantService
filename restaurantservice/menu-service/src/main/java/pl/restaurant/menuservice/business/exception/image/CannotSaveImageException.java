package pl.restaurant.menuservice.business.exception.image;

public class CannotSaveImageException extends RuntimeException {
    public CannotSaveImageException() {
        super("Wystąpił błąd. Nie można zapisać pliku na serwerze");
    }
}
