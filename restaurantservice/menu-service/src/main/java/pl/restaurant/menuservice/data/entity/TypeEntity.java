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
@Table(name = "Type")
public class TypeEntity implements Serializable, Comparable<TypeEntity> {
    private static final long serialVersionUID = 2105178182015726031L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @Column(length = 16, nullable = false)
    private String name;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MealEntity> meals;

    public TypeEntity(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(TypeEntity type) {
        return String.CASE_INSENSITIVE_ORDER.compare(type.getName(), this.getName());
    }
}
