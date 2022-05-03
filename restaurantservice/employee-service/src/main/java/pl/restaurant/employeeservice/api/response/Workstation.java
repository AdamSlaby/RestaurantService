package pl.restaurant.employeeservice.api.response;

public enum Workstation {
    CHEF("Szef kuchni"),
    MANAGER("Menedżer"),
    ADMIN("Administrator"),
    DEPUTY_CHEF("Zastępca szefa kuchni"),
    COOK("Kucharz"),
    CHEFS_ASSISTANT("Asystent kucharzy"),
    CLEANER("Zespół sprzątający"),
    WAITER("Kelner"),
    RECEPTIONIST("Recepcjonista"),
    KITCHEN_MANAGER("Kierownik kuchni");
    private String name;

    Workstation(String name) {
        this.name = name;
    }
}
