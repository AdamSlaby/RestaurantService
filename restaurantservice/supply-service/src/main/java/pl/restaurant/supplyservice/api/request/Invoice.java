package pl.restaurant.supplyservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @NotEmpty(message = "Numer faktury nie może być pusty")
    @Size(max = 20, message = "Numer faktury jest za długi")
    private String nr;

    @NotNull(message = "Identyfikator restauracji nie może być pusty")
    @Min(value = 0, message = "Identyfikator restauracji nie może być liczbą ujemną")
    private Long restaurantId;

    @NotNull(message = "Data wystawienia faktury nie może być pusta")
    private LocalDate date;

    @NotEmpty(message = "Nazwa sprzedawcy nie może być pusta")
    @Size(max = 66, message = "Nazwa sprzedawcy jest za długa")
    private String sellerName;

    @NotEmpty(message = "Nazwa kupca nie może być pusta")
    @Size(max = 66, message = "Nazwa kupca jest za długa")
    private String buyerName;

    @Valid
    private Address sellerAddress;

    @Valid
    private Address buyerAddress;

    @NotEmpty(message = "Numer NIP sprzedawcy nie może być pusty")
    @Pattern(regexp = "^\\d{10}$", message = "Numer NIP sprzedawcy jest nieprawidłowy")
    private String sellerNip;

    @NotEmpty(message = "Numer NIP kupca nie może być pusty")
    @Pattern(regexp = "^\\d{10}$", message = "Numer NIP kupca jest nieprawidłowy")
    private String buyerNip;

    @NotNull(message = "Data realizacji faktury nie może być pusta")
    private LocalDate completionDate;

    @NotNull(message = "Cena na fakturze nie może być pusta")
    @Digits(integer = 5, fraction = 2, message = "Cena na fakturze nie nieprawidłowa")
    private BigDecimal price;

    @Valid
    private List<Good> goods;
}
