package pl.restaurant.orderservice.api.response.chart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
    private String Xlabel;
    private String Ylabel;
    private String name;
    private List<ChartData> series;
}
