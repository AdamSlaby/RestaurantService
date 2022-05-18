package pl.restaurant.menuservice.business.exception.menu;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException() {
        super("Menu o podanym identyfikatorze nie istnieje");
    }
}
