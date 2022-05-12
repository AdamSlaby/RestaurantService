package pl.restaurant.newsservice.business.configuration;

public enum Role {
    MANAGER("manager"), ADMIN("admin");

    private String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
