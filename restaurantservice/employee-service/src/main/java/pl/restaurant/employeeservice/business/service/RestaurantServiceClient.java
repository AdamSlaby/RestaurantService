package pl.restaurant.employeeservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.restaurant.employeeservice.api.response.RestaurantShortInfo;

@FeignClient(url = "http://localhost:9998", name = "RESTAURANT-SERVICE")
public interface RestaurantServiceClient {
    @GetMapping("/exist/{id}")
    boolean isRestaurantExist(@PathVariable("id") Long restaurantId);
    
    @GetMapping("/short/{id}")
    RestaurantShortInfo getRestaurantShortInfo(@PathVariable("id") Long restaurantId);
}
