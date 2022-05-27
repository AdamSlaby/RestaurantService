package pl.restaurant.orderservice.business.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.restaurant.orderservice.api.response.RestaurantShortInfo;

@FeignClient(url = "http://localhost:9998", name = "RESTAURANT-SERVICE")
public interface RestaurantServiceClient {
    @GetMapping("/exist/{id}")
    boolean isRestaurantExist(@PathVariable("id") Long restaurantId);
    
    @GetMapping("/short/{id}")
    RestaurantShortInfo getRestaurantShortInfo(@PathVariable("id") Long restaurantId);

    @GetMapping("/exist/table/{id}")
    boolean isRestaurantTableExists(@PathVariable("id") Long restaurantId,
                                           @RequestParam("tableId") Long tableId);
}
