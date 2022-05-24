package pl.restaurant.orderservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.orderservice.business.service.OnlineOrderService;
import pl.restaurant.orderservice.business.service.RestaurantOrderService;

@RestController
@CrossOrigin
@AllArgsConstructor
public class OrderController {
    private OnlineOrderService onlineOrderService;
    private RestaurantOrderService restaurantOrderService;
}
