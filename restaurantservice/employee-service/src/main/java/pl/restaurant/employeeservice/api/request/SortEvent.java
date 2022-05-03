package pl.restaurant.employeeservice.api.request;

import lombok.*;
import pl.restaurant.employeeservice.api.request.SortDirection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortEvent {
    @NotBlank(message = "Nazwa sortowanej kolumny nie może być pusta")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Nazwa sortowanej kolumny jest nieprawidłowa")
    private String column;

    @NotNull(message = "Kierunek sortowania nie może być pusty")
    private SortDirection direction;
}
