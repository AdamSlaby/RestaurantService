package pl.restaurant.restaurantservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.business.service.RestaurantService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public RestaurantInfo getRestaurantInfo(@PathVariable Long id) {
        log.log(Level.INFO, "Getting restaurant of id = " + id);
        return restaurantService.getRestaurantInfo(id);
    }

    @GetMapping("/exist/{id}")
    public boolean isRestaurantExists(@PathVariable Long id) {
        log.log(Level.INFO, "Is restaurant of id = " +  id + " exist");
        return restaurantService.isRestaurantExist(id);
    }

    @GetMapping("/table")
    public Table getTable(@RequestParam("seats") Integer seats, @RequestParam("restaurantId") Long restaurantId) {
        log.log(Level.INFO, "Getting restaurant of id = " + restaurantId + " table with seats = " + seats);
        return restaurantService.getRestaurantTable(seats, restaurantId);
    }

    @GetMapping("/details/{id}")
    public Restaurant getRestaurantDetailedInfo(@PathVariable Long id) {
        log.log(Level.INFO, "Getting detailed info about restaurant of id = " + id);
        return restaurantService.getRestaurantDetailedInfo(id);
    }

    @GetMapping("/short/{id}")
    public RestaurantShortInfo getRestaurantShortInfo(@PathVariable Long id) {
        log.log(Level.INFO, "Getting short info about restaurant of id = " + id);
        return restaurantService.getRestaurantShortInfo(id);
    }

    @GetMapping()
    public List<RestaurantShortInfo> getAllRestaurants() {
        log.log(Level.INFO, "Getting all restaurants");
        return restaurantService.getAllRestaurants();
    }

    @PostMapping()
    public void addRestaurant(@RequestBody @Valid Restaurant restaurant) {
        restaurantService.addRestaurant(restaurant);
        log.log(Level.INFO, "Add new restaurant with given fields = " + restaurant);
    }

    @PutMapping("/{id}")
    public void updateRestaurant(@RequestBody @Valid Restaurant restaurant, @PathVariable Long id) {
        log.log(Level.INFO, "Updating restaurant information with given fields = " + restaurant);
        restaurantService.updateRestaurant(id, restaurant);
    }

    @DeleteMapping("/table")
    public void removeTable(@RequestParam("tableId") Long tableId, @RequestParam("restaurantId") Long restaurantId) {
        log.log(Level.INFO, "Removing from restaurant of id = " + restaurantId + " table of id = " + tableId);
        restaurantService.removeTableFromRestaurant(tableId, restaurantId);
    }
}
