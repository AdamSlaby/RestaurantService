package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Restaurant")
public class RestaurantEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(length = 31, nullable = false)
    private String email;

    @Column(length = 16, nullable = false)
    private String phoneNr;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal deliveryFee;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal minimalDeliveryPrice;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OpeningHourEntity> openingHourEntities = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RestaurantTableEntity> restaurantTables = new HashSet<>();
}
