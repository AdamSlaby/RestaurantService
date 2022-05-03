package pl.restaurant.employeeservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepo extends JpaRepository<ScheduleEntity, Long> {
    @Query("select new pl.restaurant.employeeservice.api.response" +
            ".ScheduleInfo(s.scheduleId, s.startShift, s.endShift) " +
            "from ScheduleEntity s " +
            "where s.startShift > :date")
    List<ScheduleInfo> getSchedulesFromDate(@Param("date")LocalDateTime date);
}
