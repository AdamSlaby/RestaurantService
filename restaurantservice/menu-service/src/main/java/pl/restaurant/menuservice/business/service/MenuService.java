package pl.restaurant.menuservice.business.service;

import pl.restaurant.menuservice.api.response.MenuView;

import java.util.List;

public interface MenuService {
    List<MenuView> getAllMenus();
}
