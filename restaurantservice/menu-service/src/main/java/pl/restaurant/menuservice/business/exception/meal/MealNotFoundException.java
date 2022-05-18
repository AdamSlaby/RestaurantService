package pl.restaurant.menuservice.business.exception.meal;

public class MealNotFoundException extends RuntimeException {
    public MealNotFoundException() {
        super("POsiłek o podanym identyfikatorze nie istnieje");
    }
}
