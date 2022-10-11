package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reservation")
public class ReservationEntity implements Serializable {
    private static final long serialVersionUID = 4107227850147038394L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(length = 33, nullable = false)
    private String name;

    @Column(length = 33, nullable = false)
    private String surname;

    @Column(length = 31, nullable = false)
    private String email;

    @Column(length = 16, nullable = false)
    private String phoneNr;

    @Column(nullable = false)
    private LocalDateTime fromHour;

    @Column(nullable = false)
    private LocalDateTime toHour;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<ReservationTableEntity> tables;
}
