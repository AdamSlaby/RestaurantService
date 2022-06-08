package pl.restaurant.orderservice.api.response.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartData {
    private String name;
    private Long value;

    public ChartData(String name, BigDecimal value) {
        this.name = name;
        this.value = value.longValue();
    }

    public ChartData(Integer name, Double value) {
        this.name = String.valueOf(name);
        this.value = value.longValue() / 60;
    }

    public ChartData(String name, Double value) {
        this.name = String.valueOf(name);
        this.value = value.longValue() / 60;
    }
}
