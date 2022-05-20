package pl.restaurant.supplyservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.restaurant.supplyservice.api.request.Address;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceView {
    private String nr;
    private Long restaurantId;
    private RestaurantShortInfo restaurantInfo;
    private LocalDate date;
    private String sellerName;
    private String buyerName;
    private Address sellerAddress;
    private Address buyerAddress;
    private String sellerNip;
    private String buyerNip;
    private LocalDate completionDate;
    private BigDecimal price;
    private List<GoodView> goods;
}
