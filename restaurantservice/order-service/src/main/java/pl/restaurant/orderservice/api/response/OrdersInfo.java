package pl.restaurant.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersInfo {
    private RestaurantOrderInfo restaurantOrder;
    private OnlineOrderInfo onlineOrder;
}
