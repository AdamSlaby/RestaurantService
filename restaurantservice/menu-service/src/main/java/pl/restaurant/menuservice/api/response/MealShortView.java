package pl.restaurant.menuservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealShortView {
    private Integer id;
    private String name;
}
