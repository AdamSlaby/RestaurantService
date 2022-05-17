package pl.restaurant.mailservice.business.service;

import pl.restaurant.mailservice.api.request.ReservationEmailInfo;

public interface RabbitmqListener {
    void receiveReservationMessages(ReservationEmailInfo info);
}
