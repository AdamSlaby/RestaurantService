package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.menuservice.api.response.Type;
import pl.restaurant.menuservice.business.service.TypeService;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/type")
@AllArgsConstructor
public class TypeController {
    private TypeService typeService;

    @GetMapping("/")
    public List<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @PostMapping("/")
    public Type addType(@RequestParam @Valid @Size(max = 15, message = "Nazwa typu posiłku jest za długa")
                            String type) {
        return typeService.addType(type);
    }

    @PutMapping("/{id}")
    public void updateType(@RequestParam @Valid @Size(max = 15, message = "Nazwa typu posiłku jest za długa")
                           String type, @PathVariable("id") Integer typeId) {
        typeService.updateType(typeId, type);
    }
}
