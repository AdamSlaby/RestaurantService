package pl.restaurant.menuservice.business.exception.ingredient;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(int index) {
        super(index + " Składnik o podanym identyfikatorze nie istnieje");
    }
}
