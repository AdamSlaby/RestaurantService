package pl.restaurant.menuservice.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.business.service.MenuService;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
public class MenuController {
    private MenuService menuService;

    @GetMapping("/all")
    public List<MenuView> getAllMenus() {
        return menuService.getAllMenus();
    }
}
