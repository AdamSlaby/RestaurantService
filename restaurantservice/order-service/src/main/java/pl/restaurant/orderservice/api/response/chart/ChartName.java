package pl.restaurant.orderservice.api.response.chart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@JsonFormat
public enum ChartName {
    COMPARE_ORDERS_AMOUNT("Liczba zamówień online w stosunku do liczby zamówień na miejscu"),
    COMPARE_ORDERS_INCOME("Przychód z zamówień online w stosunku do liczby zamówień na miejscu"),
    ORDERS_AMOUNT_USING_DIFFERENT_PAYMENT_METHODS("Liczba zamówień z użyciem różnych metod płatności"),
    ORDERS_AMOUNT_IN_DIFFERENT_HOURS("Liczba zamówień w zależności od godziny"),
    SOLD_DISH_AMOUNT("Liczba sprzedanych poszczególnych potraw"),
    SOLD_DISH_INCOME("Zrobek ze sprzedanych poszczególnych potraw"),
    AVERAGE_COMPLETION_ORDER_TIME_DEPENDS_ON_DISHES_AMOUNT("Średni czas realizacji zamówienia w zależności od ilości dań"),
    ORDERS_AMOUNT_CONSIDERING_DISHES_AMOUNT_IN_EACH("Ilość zamówień pod względem potraw w zamówienia");

    private String name;

    ChartName(String name) {
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
                .collect(Collectors.toMap(Enum::toString, v -> OrderType.valueOf(v.name())));
    }
}
