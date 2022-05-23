package pl.restaurant.supplyservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.business.service.UnitService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/unit")
@AllArgsConstructor
public class UnitController {
    private UnitService unitService;

    @GetMapping("/")
    public List<Unit> getAllUnits() {
        return unitService.getAllUnits();
    }
}
