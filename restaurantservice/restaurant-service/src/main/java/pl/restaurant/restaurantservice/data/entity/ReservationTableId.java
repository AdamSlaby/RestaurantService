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
    private static final long serialVersionUID = 4086854802682547113L;

    private Long reservationId;
    private RestaurantTableId tableId;
}
