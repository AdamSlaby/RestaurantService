package pl.restaurant.orderservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Restaurant_Order")
public class RestaurantOrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private Integer tableId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    private LocalDateTime deliveryDate;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RestaurantOrderMealEntity> meals;
}
