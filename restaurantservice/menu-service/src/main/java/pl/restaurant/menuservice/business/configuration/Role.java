package pl.restaurant.menuservice.business.configuration;

public enum Role {
    MANAGER("manager"), ADMIN("admin"), COMPUTER("computer"), WAITER("waiter");

    private String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
