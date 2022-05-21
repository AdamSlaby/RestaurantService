package pl.restaurant.supplyservice.business.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException() {
        super("Składnik o podanym identyfikatorze nie istnieje");
    }
}
