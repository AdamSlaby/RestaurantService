package pl.restaurant.menuservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealFilters {
    private Integer typeId;
    private String mealName;
    private SortEvent sortEvent;
    private int pageNr;
}
