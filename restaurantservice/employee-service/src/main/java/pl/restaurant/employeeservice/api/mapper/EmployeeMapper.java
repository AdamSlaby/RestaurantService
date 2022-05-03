package pl.restaurant.employeeservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.employeeservice.api.response.EmployeeShortInfo;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;

@UtilityClass
public class EmployeeMapper {
    public EmployeeShortInfo mapDataToInfo(EmployeeEntity employee) {
        return new EmployeeShortInfo().builder()
                .id(employee.getEmployeeId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .workstationId(employee.getWorkstation().getWorkstationId())
                .build();
    }
}
