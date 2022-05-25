package pl.restaurant.orderservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.restaurant.orderservice.api.request.OnlineOrder;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;
import pl.restaurant.orderservice.data.repository.OnlineOrderRepo;

@Service
@AllArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {
    private OnlineOrderRepo onlineOrderRepo;

    @Override
    public OnlineOrderEntity getOrder(Long orderId) {
        return onlineOrderRepo.getByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public String getOrderDescription(OnlineOrderEntity order) {
        StringBuilder builder = new StringBuilder();
        builder.append("Imie i nazwisko: ").append(order.getName()).append(" ").append(order.getSurname()).append("\n");
        builder.append("Email: ").append(order.getEmail()).append("\n");
        builder.append("Metoda płatności: ").append(order.getPaymentMethod().toString()).append("\n");
        builder.append("Cena do zapłaty: ").append(order.getPrice().toString()).append("\n");
        return builder.toString();
    }

    @Override
    public void reserveOrder(OnlineOrder onlineOrder) {

    }

    @Override
    public void updateOrder(Long orderId, OnlineOrder order) {

    }
}
