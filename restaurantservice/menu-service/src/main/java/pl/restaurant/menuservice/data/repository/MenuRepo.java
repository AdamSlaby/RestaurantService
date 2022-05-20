package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.menuservice.data.entity.MenuEntity;

import java.util.List;
import java.util.Optional;

public interface MenuRepo extends JpaRepository<MenuEntity, Integer> {
    @EntityGraph(attributePaths = {"meals", "meals.type", "meals.ingredients", "meals.ingredients.ingredient"})
    Optional<MenuEntity> getBySeason(String season);

    @EntityGraph(attributePaths = {"meals", "meals.type"})
    List<MenuEntity> findAllBy();

    @EntityGraph(attributePaths = {"meals"})
    Optional<MenuEntity> getByMenuId(Integer menuId);
}
