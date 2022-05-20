package pl.restaurant.menuservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private TypeEntity type;

    @ManyToMany(mappedBy = "meals", fetch = FetchType.LAZY)
    private Set<MenuEntity> menus;

    @OneToMany(mappedBy = "meal", fetch = FetchType.LAZY)
    Set<MealIngredientEntity> ingredients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealEntity that = (MealEntity) o;
        return isBest == that.isBest && Objects.equals(mealId, that.mealId) &&
                Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, name, price, imageUrl, isBest);
    }
}
