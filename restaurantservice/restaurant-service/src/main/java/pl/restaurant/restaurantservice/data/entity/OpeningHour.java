package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hourId;

    @Column(length = 1, nullable = false)
    private int weekDayNr;

    @Column(nullable = false)
    private LocalDateTime fromHour;

    @Column(nullable = false)
    private LocalDateTime toHour;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
