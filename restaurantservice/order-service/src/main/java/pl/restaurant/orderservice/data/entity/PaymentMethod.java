package pl.restaurant.orderservice.data.entity;

public enum PaymentMethod {
    CASH("Gotówka"), CARD("Karta"), PAYPAL("PayPal"), PAYU("PayU");

    private String name;

    PaymentMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
