package pl.restaurant.supplyservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.restaurant.supplyservice.api.response.RestaurantShortInfo;

@FeignClient(name = "RESTAURANT-SERVICE")
public interface RestaurantServiceClient {
    @GetMapping("/exist/{id}")
    boolean isRestaurantExist(@PathVariable("id") Long restaurantId);
    
    @GetMapping("/short/{id}")
    RestaurantShortInfo getRestaurantShortInfo(@PathVariable("id") Long restaurantId);
}
