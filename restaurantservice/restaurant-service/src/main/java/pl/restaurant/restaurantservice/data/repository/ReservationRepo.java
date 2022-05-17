package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.restaurantservice.data.entity.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<ReservationEntity, Long> {

    @Query("select r.fromHour " +
            "from ReservationEntity r " +
            "where r.reservationId = :id")
    Optional<LocalDateTime> getReservationFromHour(@Param("id") Long reservationId);

    @Query("select r from ReservationEntity r where r.fromHour > :from and r.fromHour < :to")
    List<ReservationEntity> getReservationsFromDate(@Param("from")LocalDateTime from,
                                                    @Param("to") LocalDateTime to);
}
