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
@Table(name = "Ingredient")
public class IngredientEntity implements Serializable {
    private static final long serialVersionUID = -8828808463198351237L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;

    @Column(length = 31, nullable = false)
    private String name;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    Set<MealIngredientEntity> mealIngredients;

    public IngredientEntity(String name) {
        this.name = name;
    }
}
