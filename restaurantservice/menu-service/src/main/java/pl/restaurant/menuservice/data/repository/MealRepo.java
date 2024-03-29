package pl.restaurant.menuservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.menuservice.api.response.MealShortInfo;
import pl.restaurant.menuservice.data.entity.MealEntity;

import java.util.List;
import java.util.Optional;

public interface MealRepo extends JpaRepository<MealEntity, Integer> {
    @Query("select new pl.restaurant.menuservice.api.response." +
            "MealShortInfo(m.mealId, m.type.name, m.name, m.price) " +
            "from MealEntity m " +
            "where (:tId is null or m.type.typeId = :tId) and " +
            "(:name is null or m.name like %:name%) ")
    Page<MealShortInfo> getMeals(@Param("name") String mealName,
                                 @Param("tId") Integer typeId,
                                 Pageable pageable);

    boolean existsByName(String name);

    Optional<MealEntity> findByName(String name);

    @EntityGraph(attributePaths = {"type", "ingredients", "ingredients.ingredient"})
    List<MealEntity> findAllByIsBest(boolean isBest, Pageable pageable);

    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient", "ingredients.unit"})
    List<MealEntity> getAllByMealIdIn(List<Integer> ids);

    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient", "ingredients.unit"})
    Optional<MealEntity> getByMealId(Integer id);

    @Modifying
    @Query("update MealEntity m set m.isBest = :status")
    void updateAllMealBestStatus(@Param("status") boolean status);

    @Modifying
    @Query("update MealEntity m set m.isBest = :status where m.mealId in :meals")
    void updateMealBestStatusIn(@Param("meals") List<Integer> mostPopularMeals,
                                @Param("status") boolean status);
}
