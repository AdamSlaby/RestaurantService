package pl.restaurant.supplyservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceFilters {
    private Long restaurantId;
    private String nr;
    private LocalDate date;
    private String sellerName;
    @Valid
    private SortEvent sortEvent;
    private int pageNr;
}
