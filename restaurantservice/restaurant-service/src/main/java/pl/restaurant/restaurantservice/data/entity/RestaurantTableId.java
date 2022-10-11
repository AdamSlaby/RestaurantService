package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RestaurantTableId implements Serializable {
    private static final long serialVersionUID = 6671518821589251147L;
    @Column(name = "restaurant_id")
    Long restaurantId;

    @Column(name = "table_id")
    Long tableId;
}
