package pl.restaurant.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartGenerateData {
    private PeriodType periodType;
    private String period;
    private Long placeId;
    private ChartType chartType; //think about it
    private OrderType orderType;
}
