package pl.restaurant.menuservice.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class Meal {
    @NotBlank(message = "Nazwa posiłku nie może być pusta")
    @Size(max = 30, message = "Nazwa posiłku jest za długa")
    private String name;

    @NotNull(message = "Identyfikator typu posiłku nie może być pusty")
    @Min(value = 0, message = "Indetyfikator typu posiłku nie może być liczbą ujemną")
    private Integer typeId;

    @NotNull(message = "Cena posiłku nie może być pusta")
    @Digits(integer = 3, fraction = 2, message = "Cena posiłku jest nieprawidłowa")
    private BigDecimal price;

    private MultipartFile image;

    private String ingredients;
}
