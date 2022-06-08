package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepo extends JpaRepository<RestaurantEntity, Long> {
    @Query("select new pl.restaurant.restaurantservice.api.response" +
            ".RestaurantShortInfo(r.restaurantId, r.address.city, r.address.street) " +
            "from RestaurantEntity r")
    List<RestaurantShortInfo> getAllRestaurants();

    @Query("select new pl.restaurant.restaurantservice.api.response" +
            ".RestaurantShortInfo(r.restaurantId, r.address.city, r.address.street) " +
            "from RestaurantEntity r " +
            "where r.restaurantId = :id")
    Optional<RestaurantShortInfo> getRestaurant(@Param("id") Long restaurantId);

    Optional<RestaurantInfo> findByRestaurantId(Long restaurantId);

    boolean existsByRestaurantId(Long restaurantId);

    @Query("select r.deliveryFee from RestaurantEntity r where r.restaurantId = :id")
    Optional<BigDecimal> getRestaurantDeliveryFee(@Param("id") Long id);
}
