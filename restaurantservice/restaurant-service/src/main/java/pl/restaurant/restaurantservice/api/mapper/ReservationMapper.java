package pl.restaurant.restaurantservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.restaurantservice.api.request.Reservation;
import pl.restaurant.restaurantservice.api.response.ReservationEmailInfo;
import pl.restaurant.restaurantservice.api.response.ReservationInfo;
import pl.restaurant.restaurantservice.api.response.ReservationShortInfo;
import pl.restaurant.restaurantservice.api.response.RestaurantShortInfo;
import pl.restaurant.restaurantservice.data.entity.ReservationEntity;
import pl.restaurant.restaurantservice.data.entity.ReservationTableEntity;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ReservationMapper {
    public static ReservationInfo mapDataToInfo(ReservationEntity reservation, Set<ReservationTableEntity> tables) {
        return new ReservationInfo().builder()
                .id(reservation.getReservationId())
                .tableIds(tables.stream()
                        .map(el -> el.getRestaurantTable().getTable().getTableId())
                        .collect(Collectors.toList()))
                .name(reservation.getName())
                .surname(reservation.getSurname())
                .email(reservation.getEmail())
                .phoneNr(reservation.getPhoneNr())
                .fromHour(reservation.getFromHour())
                .toHour(reservation.getToHour())
                .build();
    }

    public static ReservationShortInfo mapDataToShosrtInfo(ReservationEntity reservation) {
        return new ReservationShortInfo().builder()
                .id(reservation.getReservationId())
                .tableIds(reservation.getTables().stream()
                        .map(el -> el.getRestaurantTable().getTable().getTableId())
                        .collect(Collectors.toList()))
                .fromHour(reservation.getFromHour())
                .toHour(reservation.getToHour())
                .build();
    }

    public static ReservationEntity mapObjectToData(Reservation reservation) {
        return new ReservationEntity().builder()
                .name(reservation.getName())
                .surname(reservation.getSurname())
                .email(reservation.getEmail())
                .phoneNr(reservation.getPhoneNr())
                .fromHour(reservation.getFromHour())
                .toHour(reservation.getFromHour().plusHours(2))
                .build();
    }

    public static ReservationEmailInfo mapDataToEmailInfo(ReservationEntity reservationEntity, Reservation reservation
            , RestaurantShortInfo restaurant) {
        return new ReservationEmailInfo().builder()
                .id(reservationEntity.getReservationId())
                .restaurantInfo(restaurant.getCity() + " ul." + restaurant.getStreet())
                .tableIds(reservation.getTableIds())
                .name(reservation.getName())
                .surname(reservation.getSurname())
                .email(reservation.getEmail())
                .phoneNr(reservation.getPhoneNr())
                .fromHour(reservation.getFromHour())
                .toHour(reservationEntity.getToHour())
                .build();
    }
}
