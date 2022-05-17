package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTableId implements Serializable {
    Long reservationId;
    RestaurantTableId tableId;
}
