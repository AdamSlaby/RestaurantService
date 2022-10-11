package pl.restaurant.menuservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Unit")
public class UnitEntity implements Serializable {
    private static final long serialVersionUID = 7479896174874980556L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitId;

    @Column(length = 5, nullable = false)
    private String name;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MealIngredientEntity> mealIngredients;
}
