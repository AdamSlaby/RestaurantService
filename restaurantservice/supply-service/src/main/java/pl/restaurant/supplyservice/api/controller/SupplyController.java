package pl.restaurant.supplyservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.supplyservice.business.service.SupplyService;

@RestController
@CrossOrigin
@AllArgsConstructor
public class SupplyController {
    private SupplyService supplyService;


}
