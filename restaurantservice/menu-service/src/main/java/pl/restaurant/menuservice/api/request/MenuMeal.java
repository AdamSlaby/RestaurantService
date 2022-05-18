package pl.restaurant.menuservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuMeal {
    @NotBlank(message = "Nazwa lub identyfikator posiłku nie może być pusty")
    private String meal;

    @NotNull(message = "Identyfikator menu nie może być pusty")
    @Min(value = 0, message = "Identyfikator posiłku nie może być liczbą ujemną")
    private Integer menuId;
}
