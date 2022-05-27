package pl.restaurant.orderservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Online_Order")
public class OnlineOrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(length = 33, nullable = false)
    private String name;

    @Column(length = 33, nullable = false)
    private String surname;

    @Column(length = 31, nullable = false)
    private String email;

    @Column(length = 16, nullable = false)
    private String phoneNr;

    private Integer floor;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    private LocalDateTime timeToPaid;

    @Column(nullable = false)
    private boolean isPaid;

    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OnlineOrderMealEntity> meals;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;
}
