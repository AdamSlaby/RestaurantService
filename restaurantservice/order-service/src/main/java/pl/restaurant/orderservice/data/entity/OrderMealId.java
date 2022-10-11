package pl.restaurant.orderservice.data.entity;

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
public class OrderMealId implements Serializable {
    private static final long serialVersionUID = -5226507871198692364L;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "meal_id")
    private Integer mealId;
}
