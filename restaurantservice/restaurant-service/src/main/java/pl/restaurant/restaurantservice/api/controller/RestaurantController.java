package pl.restaurant.restaurantservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.business.service.RestaurantService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@Log4j2
@Validated
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public RestaurantInfo getRestaurantInfo(@PathVariable Long id) {
        log.log(Level.INFO, "Getting restaurant of id = " + id);
        return restaurantService.getRestaurantInfo(id);
    }

    @GetMapping("/details/{id}")
    public Restaurant getRestaurantDetailedInfo(@PathVariable Long id) {
        log.log(Level.INFO, "Getting detailed info about restaurant of id = " + id);
        return restaurantService.getRestaurantDetailedInfo(id);
    }

    @GetMapping()
    public List<RestaurantShortInfo> getAllRestaurants() {
        log.log(Level.INFO, "Getting all restaurants");
        return restaurantService.getAllRestaurants();
    }

    @PostMapping()
    public void addRestaurant(@RequestBody @Valid Restaurant restaurant) {
        log.log(Level.INFO, "Add new restaurant with given fields = " + restaurant);
        restaurantService.addRestaurant(restaurant);
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@RequestBody @Valid Restaurant restaurant, @PathVariable Long id) {
        log.log(Level.INFO, "Updating restaurant information with given fields = " + restaurant);
        return restaurantService.updateRestaurant(id, restaurant);
    }
}
