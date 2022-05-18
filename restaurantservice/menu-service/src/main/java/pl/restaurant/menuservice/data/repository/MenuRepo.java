package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.menuservice.data.entity.MenuEntity;

import java.util.Optional;

public interface MenuRepo extends JpaRepository<MenuEntity, Integer> {
    Optional<MenuEntity> getBySeason(String season);
}
