package pl.restaurant.menuservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.menuservice.api.response.IngredientInfo;
import pl.restaurant.menuservice.data.entity.IngredientEntity;

import java.util.List;
import java.util.Optional;

public interface IngredientRepo extends JpaRepository<IngredientEntity, Integer> {
    @Query("select new pl.restaurant.menuservice.api.response." +
            "IngredientInfo(i.ingredientId, i.name) " +
            "from IngredientEntity i")
    List<IngredientInfo> getAllIngredients();

    @Query("select i.ingredientId from IngredientEntity i where i.name = :name")
    Optional<Integer> getIngredientByName(@Param("name") String name);

    boolean existsByName(String name);

    boolean existsAllByIngredientIdIn(List<Integer> ingredientIds);
}
