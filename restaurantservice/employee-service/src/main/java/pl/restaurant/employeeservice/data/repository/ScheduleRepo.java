package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<ScheduleEntity, Long> {
    @Query("select new pl.restaurant.employeeservice.api.response" +
            ".ScheduleInfo(s.scheduleId, s.startShift, s.endShift) " +
            "from ScheduleEntity s " +
            "where s.startShift > :date and s.employee.employeeId = :eId")
    List<ScheduleInfo> getSchedulesFromDate(@Param("date") LocalDateTime date,
                                            @Param("eId") Long employeeId);

    @Query("select s " +
            "from ScheduleEntity s " +
            "where (((s.startShift <= :startDate and  s.endShift >= :endDate) or " +
            "(s.startShift > :startDate and  s.endShift < :endDate) or " +
            "(s.startShift > :startDate and s.startShift < :endDate and  s.endShift >= :endDate) or " +
            "(s.startShift <= :startDate and s.endShift > :startDate and  s.endShift < :endDate)) and " +
            "s.employee.employeeId = :id)")
    Optional<ScheduleEntity> getScheduleByDateAndEmployeeId(@Param("startDate") LocalDateTime from,
                                                            @Param("endDate") LocalDateTime to,
                                                            @Param("id") Long employeeId);
}
