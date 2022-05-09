package pl.restaurant.employeeservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Workstation")
public class WorkstationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workstationId;

    @Column(length = 30, nullable = false)
    private String name;

    @OneToMany(mappedBy = "workstation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EmployeeEntity> employees = new HashSet<>();
}
