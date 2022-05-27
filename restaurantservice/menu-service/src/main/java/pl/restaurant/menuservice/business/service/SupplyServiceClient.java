package pl.restaurant.menuservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.restaurant.menuservice.api.response.OrderValidation;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "SUPPLY-SERVICE")
public interface SupplyServiceClient {

    @PostMapping("/{id}")
    void checkSupplies(@PathVariable("id") Long restaurantId,
                              @RequestBody @Valid OrderValidation order);
    @PutMapping("/{id}")
    void updateSupplies(@PathVariable("id") Long restaurantId,
                               @RequestBody @Valid List<OrderValidation> orders);

    @PutMapping("/rollback/{id}")
    void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                                      @RequestBody @Valid List<OrderValidation> orders);
}
