package pl.restaurant.employeeservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Employee")
public class EmployeeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(length = 33, nullable = false)
    private String name;

    @Column(length = 33, nullable = false)
    private String surname;

    @Column(length = 12, nullable = false)
    private String pesel;

    @Column(length = 14, nullable = false)
    private String phoneNr;

    @Column(length = 27, nullable = false)
    private String accountNr;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDate employmentDate;

    private LocalDate dismissalDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "workstation_id", nullable = false)
    private WorkstationEntity workstation;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "Employee_Schedule",
                joinColumns = {@JoinColumn(name = "employee_id")},
                inverseJoinColumns = {@JoinColumn(name = "schedule_id")})
    private Set<ScheduleEntity> schedules = new HashSet<>();
}
