package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reservation_Tables")
public class ReservationTableEntity {
    @EmbeddedId
    ReservationTableId reservationTableId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id")
    ReservationEntity reservation;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tableId")
    @JoinColumns( {
            @JoinColumn(name="restaurant_id", referencedColumnName="restaurant_id"),
            @JoinColumn(name="table_id", referencedColumnName="table_id")
    } )
    RestaurantTableEntity restaurantTable;
}
