package pl.restaurant.orderservice.business.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.orderservice.api.request.Order;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "MENU-SERVICE")
public interface MenuServiceClient {
    @PostMapping("/meal/meals/{id}")
    String validateOrders(@PathVariable("id") Long restaurantId,
                          @RequestBody @Valid List<Order> orders,
                          @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/meal/rollback/{id}")
    void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                               @RequestBody @Valid List<Order> orders,
                               @RequestHeader("Authorization") String authorizationHeader);
}
