package pl.restaurant.menuservice.business.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.response.OrderValidation;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "SUPPLY-SERVICE")
public interface SupplyServiceClient {

    @PostMapping("/{id}")
    void checkSupplies(@PathVariable("id") Long restaurantId,
                              @RequestBody @Valid OrderValidation order);
    @PutMapping("/update/{id}")
    void updateSupplies(@PathVariable("id") Long restaurantId,
                        @RequestBody @Valid List<OrderValidation> orders,
                        @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/rollback/{id}")
    void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                               @RequestBody @Valid List<OrderValidation> orders,
                               @RequestHeader(value = "Authorization") String authorizationHeader);
}
