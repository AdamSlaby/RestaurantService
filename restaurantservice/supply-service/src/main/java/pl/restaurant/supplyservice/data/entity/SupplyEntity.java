package pl.restaurant.supplyservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Supply")
public class SupplyEntity implements Serializable {
    @EmbeddedId
    private RestaurantIngredientId restaurantIngredientId;

    @Column(precision = 8, scale = 3, nullable = false)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitEntity unit;
}
