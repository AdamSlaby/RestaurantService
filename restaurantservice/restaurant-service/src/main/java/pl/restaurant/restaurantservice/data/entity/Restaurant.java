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
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(length = 31, nullable = false)
    private String email;

    @Column(length = 14, nullable = false)
    private String phoneNr;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal deliveryFee;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal minimalDeliveryPrice;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OpeningHour> openingHours = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "Restaurant_Tables",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "table_id")})
    private Set<Table> tables = new HashSet<>();
}
