package pl.restaurant.supplyservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    @NotNull(message = "Identyfikator składnika nie może być pusty")
    @Min(value = 0, message = "Identyfikator składnika nie może być liczbą ujemną")
    private Integer ingredientId;

    @NotNull(message = "Ilość składnika nie może być pusta")
    @Min(value = 0, message = "Ilość składnika nie może być liczbą ujemną")
    @Digits(integer = 5, fraction = 2, message = "Ilość składnika jest nieprawidłowa")
    private BigDecimal quantity;

    @NotNull(message = "Identyfikator jednostki miary jest nieprawidłowy")
    @Min(value = 0, message = "Identyfikator jednostki miary nie może być liczbą ujemną")
    private Integer unitId;

    @NotNull(message = "Cena jednostkowa towaru netto nie może być pusta")
    @Min(value = 0, message = "Cena jednostkowa towaru netto nie może być liczbą ujemną")
    @Digits(integer = 3, fraction = 2, message = "Cena jednostkowa towaru netto jest nieprawidłowa")
    private BigDecimal unitNetPrice;

    @NotNull(message = "Wartość zniżki nie może być pusta")
    @Min(value = 0, message = "Wartość zniżki nie może być liczbą ujemną")
    @Digits(integer = 4, fraction = 2, message = "Wartość zniżki jest nieprawidłowa")
    private BigDecimal discount;

    @NotNull(message = "Cena netto towaru nie może być pusta")
    @Min(value = 0, message = "Cena netto towaru nie może być liczbą ujemną")
    @Digits(integer = 5, fraction = 2, message = "Cena netto towaru jest nieprawidłowa")
    private BigDecimal netPrice;

    @NotNull(message = "Typ opodatkowania towaru nie może być pusty")
    private TaxType taxType;

    @NotNull(message = "Wartość podatku nie może być pusta")
    @Digits(integer = 4, fraction = 2, message = "Wartość podatku jest nieprawidłowa")
    private BigDecimal taxPrice;
}
