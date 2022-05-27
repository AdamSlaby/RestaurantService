package pl.restaurant.orderservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.restaurant.orderservice.api.response.chart.ChartName;
import pl.restaurant.orderservice.api.response.chart.OrderType;
import pl.restaurant.orderservice.api.response.chart.PeriodType;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateChartOptions {
    private PeriodType periodType;

    @NotEmpty(message = "Okres nie może być pusty")
    private String period;
    private Long placeId;
    private ChartName chartName;
    private OrderType orderType;
}
