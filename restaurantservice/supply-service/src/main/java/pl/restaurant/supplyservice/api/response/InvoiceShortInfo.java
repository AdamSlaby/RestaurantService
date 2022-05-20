package pl.restaurant.supplyservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceShortInfo {
    private String nr;
    private LocalDate date;
    private String sellerName;
    private BigDecimal price;
}
