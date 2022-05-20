package pl.restaurant.menuservice.business.exception.ingredient;

public class IngredientAlreadyExistsException extends RuntimeException {
    public IngredientAlreadyExistsException(int index) {
        super(index + " Składnik o podanej nazwie już istnieje");
    }

    public IngredientAlreadyExistsException() {
        super("Składnik o podanej nazwie już istnieje");
    }
}
