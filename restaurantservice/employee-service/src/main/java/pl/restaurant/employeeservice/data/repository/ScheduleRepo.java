package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;

public interface ScheduleRepo extends JpaRepository<ScheduleEntity, Long> {
}
