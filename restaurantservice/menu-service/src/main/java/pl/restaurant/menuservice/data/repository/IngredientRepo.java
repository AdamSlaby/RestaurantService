package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.data.entity.IngredientEntity;

import java.util.List;

public interface IngredientRepo extends JpaRepository<IngredientEntity, Integer> {
    @Query("select new pl.restaurant.menuservice.api.response." +
            "IngredientInfo(i.ingredientId, i.name) " +
            "from IngredientEntity i")
    List<IngredientInfo> getAllIngredients();

    boolean existsByName(String name);
}
