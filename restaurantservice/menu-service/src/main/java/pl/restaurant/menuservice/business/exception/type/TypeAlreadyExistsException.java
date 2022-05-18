package pl.restaurant.menuservice.business.exception.type;

public class TypeAlreadyExistsException extends RuntimeException {
    public TypeAlreadyExistsException() {
        super("Typ posiłku o podanej nazwie już istnieje");
    }
}
