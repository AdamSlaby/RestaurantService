package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(length = 60, nullable = false)
    private String city;

    @Column(length = 60, nullable = false)
    private String street;

    @Column(length = 6, nullable = false)
    private String houseNr;

    @Column(length = 4, nullable = false)
    private String flatNr;

    @Column(length = 7, nullable = false)
    private String postcode;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Restaurant restaurant;
}
