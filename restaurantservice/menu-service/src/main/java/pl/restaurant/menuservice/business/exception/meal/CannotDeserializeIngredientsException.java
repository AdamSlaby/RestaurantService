package pl.restaurant.menuservice.business.exception.meal;

public class CannotDeserializeIngredientsException extends RuntimeException {
    public CannotDeserializeIngredientsException() {
        super("Wystąpił błąd nie można zdeseralizować listy składników");
    }
}
