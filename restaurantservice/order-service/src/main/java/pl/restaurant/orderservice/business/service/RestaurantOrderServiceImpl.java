package pl.restaurant.orderservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.data.repository.RestaurantOrderRepo;

@Service
@AllArgsConstructor
public class RestaurantOrderServiceImpl implements RestaurantOrderService {
    private RestaurantOrderRepo orderRepo;
    @Override
    public void addRestaurantOrder() {

    }

    @Override
    public void updateOrder(Long orderId, RestaurantOrder order) {

    }
}
