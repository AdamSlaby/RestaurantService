package pl.restaurant.restaurantservice.business.service;

import pl.restaurant.restaurantservice.api.request.Reservation;
import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.api.response.ReservationInfo;
import pl.restaurant.restaurantservice.api.response.ReservationShortInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationInfo getReservationInfo(Long reservationId);

    List<ReservationShortInfo> getReservations(LocalDateTime dateTime, Long restaurantId);

    List<AvailableHour> getAvailableHours(Long restaurantId, LocalDateTime dateTime, int peopleNr);

    void addReservation(Reservation reservation);

    void deleteReservation(Long reservationId);
}
