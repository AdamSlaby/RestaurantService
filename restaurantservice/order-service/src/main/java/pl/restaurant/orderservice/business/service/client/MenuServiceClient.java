package pl.restaurant.orderservice.business.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.restaurant.orderservice.api.request.Order;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "MENU-SERVICE")
public interface MenuServiceClient {
    @PostMapping("/meal/meals/{id}")
    String validateOrders(@PathVariable("id") Long restaurantId, @RequestBody @Valid List<Order> orders);

    @PostMapping("/meal/rollback/{id}")
    void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                                      @RequestBody @Valid List<Order> orders);
}
