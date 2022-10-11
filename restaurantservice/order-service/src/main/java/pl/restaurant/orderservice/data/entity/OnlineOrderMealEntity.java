package pl.restaurant.orderservice.data.entity;

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
@Table(name = "OnlineOrder_Meal")
public class OnlineOrderMealEntity implements Serializable {
    private static final long serialVersionUID = 9032971575683446006L;

    @EmbeddedId
    private OrderMealId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OnlineOrderEntity order;

    @Column(length = 31, nullable = false)
    private String mealName;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;
}
