package pl.restaurant.menuservice.business.exception.meal;

public class MealAlreadyExistsException extends RuntimeException {
    public MealAlreadyExistsException() {
        super("Posiłek o podanej nazwie już istnieje");
    }
}
