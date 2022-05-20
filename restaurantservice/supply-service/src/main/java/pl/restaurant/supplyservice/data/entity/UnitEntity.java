package pl.restaurant.supplyservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Unit")
public class UnitEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitId;

    @Column(length = 5, nullable = false)
    private String name;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SupplyEntity> supplies;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<GoodEntity> goods;
}
