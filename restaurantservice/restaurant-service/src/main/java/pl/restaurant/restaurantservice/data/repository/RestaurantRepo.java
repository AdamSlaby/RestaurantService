package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepo extends JpaRepository<RestaurantEntity, Long> {
    @Query("select new pl.restaurant.restaurantservice.api.response" +
            ".RestaurantShortInfo(r.restaurantId, r.address.city, r.address.street) " +
            "from RestaurantEntity r")
    List<RestaurantShortInfo> getAllRestaurants();

    Optional<RestaurantInfo> findByRestaurantId(Long restaurantId);
}
