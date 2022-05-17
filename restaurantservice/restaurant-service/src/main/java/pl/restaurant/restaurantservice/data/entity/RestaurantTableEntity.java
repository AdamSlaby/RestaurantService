package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Restaurant_Tables")
public class RestaurantTableEntity implements Serializable {
    @EmbeddedId
    RestaurantTableId restaurantTableId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tableId")
    @JoinColumn(name = "table_id")
    FoodTableEntity table;

    @OneToMany(mappedBy = "restaurantTable", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<ReservationTableEntity> reservationTables;
}
