package pl.restaurant.restaurantservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.restaurantservice.data.entity.ReservationEntity;
import pl.restaurant.restaurantservice.data.entity.ReservationTableEntity;
import pl.restaurant.restaurantservice.data.entity.ReservationTableId;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationTableRepo extends JpaRepository<ReservationTableEntity, ReservationTableId> {
    @Modifying
    @Query("delete from ReservationTableEntity r where r.reservation.reservationId = :id")
    void deleteAllByReservationId(@Param("id") Long reservationId);

    @Query("select count(r) " +
            "from ReservationTableEntity r " +
            "where r.restaurantTable.table.tableId in :list and r.reservation.fromHour = :date")
    Long isReservationExists(@Param("list") List<Long> tableIds,
                             @Param("date") LocalDateTime fromHour);

    @Query("select distinct r.reservation from ReservationTableEntity r where r.reservation.fromHour > :from " +
            "and r.reservation.fromHour < :to " +
            "and (:id is null or r.restaurantTable.restaurant.restaurantId = :id )")
    List<ReservationEntity> getReservationsFromDate(@Param("from")LocalDateTime from,
                                                    @Param("to") LocalDateTime to,
                                                    @Param("id") Long restaurantId);
}
