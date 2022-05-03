package pl.restaurant.employeeservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.data.entity.ScheduleEntity;

@UtilityClass
public class ScheduleMapper {
    public ScheduleInfo mapDataToInfo(ScheduleEntity schedule) {
        return new ScheduleInfo().builder()
                .id(schedule.getScheduleId())
                .startShift(schedule.getStartShift())
                .endShift(schedule.getEndShift())
                .build();
    }
}
