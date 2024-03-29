package pl.restaurant.supplyservice.business.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "MENU-SERVICE")
public interface MenuServiceClient {
    @PostMapping("/ingredient/exists")
    boolean isIngredientsExists(@RequestBody List<@NotNull Integer> ingredientIds);

    @PostMapping("/ingredient/name")
    Integer getIngredient(@RequestBody String ingredient,
                          @RequestHeader(value = "Authorization") String authorizationHeader);
}
