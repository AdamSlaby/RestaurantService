package pl.restaurant.orderservice.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import pl.restaurant.orderservice.api.response.chart.OrderType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@JsonFormat
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

    @JsonValue
    public String getValue() {
        return name;
    }

    @JsonCreator
    public static OrderType fromValue(String value) {
        return getMap(OrderType.class).get(value);
    }

    public static Map<String, OrderType> getMap(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants())
                .collect(Collectors.toMap(Enum::toString, v -> OrderType.valueOf(v.toString())));
    }
}
