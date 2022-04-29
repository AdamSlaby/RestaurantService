package pl.restaurant.restaurantservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.restaurantservice.api.mapper.RestaurantMapper;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    @Override
    public RestaurantInfo getRestaurantInfo(Long id) {
        return RestaurantMapper.mapRestaurantToInfo(restaurantRepo.findById(id)
                .orElseThrow(RestaurantNotFoundException::new));
    }

    @Override
    public Restaurant getRestaurantDetailedInfo(Long id) {
        return null;
    }

    @Override
    public List<RestaurantShortInfo> getAllRestaurants() {
        return null;
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {

    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        return null;
    }
}
