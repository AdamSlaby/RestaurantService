package pl.restaurant.menuservice.business.exception.type;

public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException() {
        super("Typ o podanym identyfikatorze nie istnieje");
    }
}
