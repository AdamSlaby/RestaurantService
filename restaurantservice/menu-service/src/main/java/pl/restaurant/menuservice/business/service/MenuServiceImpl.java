package pl.restaurant.menuservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.data.repository.MenuRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {
    private MenuRepo menuRepo;
    @Override
    public List<MenuView> getAllMenus() {
        return null;
    }
}
