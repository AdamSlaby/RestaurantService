package pl.restaurant.orderservice.api.response.chart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@JsonFormat
public enum PeriodType {
    DAY("Dzień"), MONTH("Miesiąc"), YEAR("Rok");

    private String name;

    PeriodType(String name) {
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
