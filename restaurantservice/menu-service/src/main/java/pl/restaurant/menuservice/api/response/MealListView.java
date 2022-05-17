package pl.restaurant.menuservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealListView {
    private Long totalElements;
    private List<MealShortInfo> meals;
}
