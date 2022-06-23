package pl.restaurant.menuservice.api.request;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @NotNull(message = "Identyfikator składnika nie może być pusty")
    private Integer id;

    @NotBlank(message = "Nazwa składnika nie może być pusta")
    @Size(max = 30, message = "Nazwa składnika jest za długa")
    private String name;

    @NotNull(message = "Ilość składnika nie może być pusta")
    @Digits(integer = 3, fraction = 2, message = "Ilość składnika jest nieprawidłowa")
    @Min(value = 0, message = "Ilość składnika nie może być liczbą ujemną")
    private BigDecimal amount;

    @NotNull(message = "Identyfikator jednostki miary nie może być pusty")
    @Min(value = 0, message = "Indetyfikator jednostki miary nie może być liczbą ujemną")
    private Integer unitId;
}
