package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.MenuEntity;

public interface MenuRepo extends JpaRepository<MenuEntity, Integer> {
}
