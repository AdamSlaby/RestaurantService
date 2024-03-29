package pl.restaurant.supplyservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.supplyservice.api.request.NewSupply;
import pl.restaurant.supplyservice.api.request.OrderValidation;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.business.service.SupplyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class SupplyController {
    private SupplyService supplyService;

    @GetMapping("/list/{id}")
    public List<SupplyInfo> getAllSupplies(@PathVariable("id") Long restaurantId) {
        return supplyService.getAllSupplies(restaurantId);
    }

    @PostMapping("/")
    public SupplyInfo addSupply(@RequestBody @Valid NewSupply newSupply, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return supplyService.addSupply(newSupply, authorization);
    }

    @PostMapping("/{id}")
    public void checkSupplies(@PathVariable("id") Long restaurantId,
                               @RequestBody @Valid OrderValidation order) {
        supplyService.checkSupplies(restaurantId, order);
    }

    @PutMapping("/update/{id}")
    public void updateSupplies(@PathVariable("id") Long restaurantId,
                                 @RequestBody @Valid List<OrderValidation> orders) {
        supplyService.updateSupplies(restaurantId, orders);
    }

    @PutMapping("/rollback/{id}")
    public void rollbackOrderSupplies(@PathVariable("id") Long restaurantId,
                                      @RequestBody @Valid List<OrderValidation> orders) {
        supplyService.rollbackOrderSupplies(restaurantId, orders);
    }

    @PutMapping("/")
    public void updateSupplyList(@RequestBody @Valid SupplyInfo supply) {
        supplyService.updateSupply(supply);
    }
}
