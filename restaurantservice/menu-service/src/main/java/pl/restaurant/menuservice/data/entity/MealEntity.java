package pl.restaurant.menuservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Meal")
public class MealEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mealId;

    @Column(length = 31, nullable = false)
    private String name;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(length = 101, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isBest;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private TypeEntity type;

    @ManyToMany(mappedBy = "meals", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MenuEntity> menus;

    @OneToMany(mappedBy = "meal", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<MealIngredientEntity> ingredients;
}
