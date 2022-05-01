package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.restaurantservice.data.entity.OpeningHourEntity;

import java.util.List;

public interface OpeningHourRepo extends JpaRepository<OpeningHourEntity, Long> {
    @Query("select h from OpeningHourEntity h where h.restaurant.restaurantId = :id order by h.hourId ASC ")
    List<OpeningHourEntity> getHoursByRestaurant(@Param("id") Long restaurantId);
}
