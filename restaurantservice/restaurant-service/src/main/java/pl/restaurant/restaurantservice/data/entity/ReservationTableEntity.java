package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reservation_Tables")
public class ReservationTableEntity implements Serializable {
    private static final long serialVersionUID = -6915558578910647541L;

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
