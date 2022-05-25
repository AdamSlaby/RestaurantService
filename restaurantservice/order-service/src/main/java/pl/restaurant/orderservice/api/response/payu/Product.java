package pl.restaurant.orderservice.api.response.payu;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String unitPrice;
    private String quantity;
}
