package pl.restaurant.supplyservice.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RestaurantIngredientId implements Serializable {
    private static final long serialVersionUID = 8943738640176798025L;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "ingredient_id")
    private Integer ingredientId;
}
