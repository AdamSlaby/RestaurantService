package pl.restaurant.mailservice.business.service;

import pl.restaurant.mailservice.api.request.OrderEmailInfo;
import pl.restaurant.mailservice.api.request.ReservationEmailInfo;

public interface RabbitmqListener {
    void receiveReservationMessage(ReservationEmailInfo info);

    void receiveOrderMessage(OrderEmailInfo info);
}
