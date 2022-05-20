package pl.restaurant.menuservice.business.exception.meal;

public class MealAlreadyAddedToMenuException extends RuntimeException {
    public MealAlreadyAddedToMenuException() {
        super("Posiłek już został dodany do danego menu.");
    }
}
