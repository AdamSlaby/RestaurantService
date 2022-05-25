package pl.restaurant.orderservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.api.request.OrderFilters;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.api.response.ActiveOrder;
import pl.restaurant.orderservice.api.response.OrderListView;
import pl.restaurant.orderservice.api.response.OrdersInfo;
import pl.restaurant.orderservice.business.service.OnlineOrderService;
import pl.restaurant.orderservice.business.service.OrderService;
import pl.restaurant.orderservice.business.service.RestaurantOrderService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class OrderController {
    private OnlineOrderService onlineOrderService;
    private RestaurantOrderService restaurantOrderService;
    private OrderService orderService;

    @PostMapping("/list")
    public OrderListView getOrderList(@RequestBody @Valid OrderFilters filters) {
        return orderService.getOrderList(filters);
    }

    @GetMapping("/{id}")
    public OrdersInfo getOrderInfo(@PathVariable("id") Long orderId, @RequestParam("type") String type) {
        return orderService.getOrderInfo(orderId, type);
    }

    @GetMapping("/active")
    public List<ActiveOrder> getActiveOrders(@RequestParam(value = "rId", required = false) Long restaurantId) {
        return orderService.getActiveOrders(restaurantId);
    }

    @PostMapping("/restaurant")
    public void addRestaurantOrder(@RequestBody @Valid RestaurantOrder order) {
        restaurantOrderService.addRestaurantOrder();
    }

    @PostMapping("/reserve")
    public Long reserveOrder(@RequestBody @Valid OnlineOrder onlineOrder) {
        onlineOrderService.reserveOrder(onlineOrder);
    }

    @PutMapping("/{id}")
    public void updateOnlineOrder(@PathVariable("id") Long orderId, @RequestBody @Valid OnlineOrder order) {
        onlineOrderService.updateOrder(orderId, order);
    }

    @PutMapping("/{id}")
    public void updateRestaurantOrder(@PathVariable("id") Long orderId, @RequestBody @Valid RestaurantOrder order) {
        restaurantOrderService.updateOrder(orderId, order);
    }
}
