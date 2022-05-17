package pl.restaurant.menuservice.api.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuView {
    private Integer id;
    private String season;
    private Map<String, List<MealShortView>> mealMap;
}
