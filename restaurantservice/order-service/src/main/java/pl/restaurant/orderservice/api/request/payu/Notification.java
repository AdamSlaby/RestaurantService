package pl.restaurant.orderservice.api.request.payu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Order order;
    private String localReceiptDateTime;
    private List<Property> properties;
}
