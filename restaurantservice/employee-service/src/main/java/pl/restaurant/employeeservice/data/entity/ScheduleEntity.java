package pl.restaurant.employeeservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Schedule")
public class ScheduleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false)
    private LocalDateTime startShift;

    @Column(nullable = false)
    private LocalDateTime endShift;

    @ManyToMany(mappedBy = "schedules", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EmployeeEntity> employees = new HashSet<>();
}
