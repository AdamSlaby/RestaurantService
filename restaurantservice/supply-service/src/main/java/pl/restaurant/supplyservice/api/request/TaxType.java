package pl.restaurant.supplyservice.api.request;

public enum TaxType {
    A("A"), B("B"), C("C"), D("D"), E("E");

    private String name;

    TaxType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
