package pl.restaurant.restaurantservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Food_Table")
public class FoodTableEntity implements Serializable {
    private static final long serialVersionUID = -8765574881346400292L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;

    @Column(length = 2, nullable = false)
    private int seatsNr;

    @OneToMany(mappedBy = "table", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RestaurantTableEntity> restaurantTables = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodTableEntity that = (FoodTableEntity) o;
        return seatsNr == that.seatsNr && Objects.equals(tableId, that.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, seatsNr);
    }

    public FoodTableEntity(Long tableId, int seatsNr) {
        this.tableId = tableId;
        this.seatsNr = seatsNr;
    }
}
