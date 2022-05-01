package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;

    @Column(length = 2, nullable = false)
    private int seatsNr;

    @ManyToMany(mappedBy = "tables", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RestaurantEntity> restaurants = new HashSet<>();
}
