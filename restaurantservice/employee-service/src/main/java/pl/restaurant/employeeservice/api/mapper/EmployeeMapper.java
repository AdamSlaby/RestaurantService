package pl.restaurant.employeeservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.employeeservice.api.request.Employee;
import pl.restaurant.employeeservice.api.response.EmployeeInfo;
import pl.restaurant.employeeservice.api.response.EmployeeShortInfo;
import pl.restaurant.employeeservice.api.response.RestaurantShortInfo;
import pl.restaurant.employeeservice.api.response.ScheduleInfo;
import pl.restaurant.employeeservice.data.entity.AddressEntity;
import pl.restaurant.employeeservice.data.entity.EmployeeEntity;
import pl.restaurant.employeeservice.data.entity.WorkstationEntity;

import java.util.List;

@UtilityClass
public class EmployeeMapper {
    public EmployeeShortInfo mapDataToShortInfo(EmployeeEntity employee) {
        return new EmployeeShortInfo().builder()
                .id(employee.getEmployeeId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .workstationId(employee.getWorkstation().getWorkstationId())
                .build();
    }

    public EmployeeInfo mapDataToInfo(EmployeeEntity employee, RestaurantShortInfo restaurantInfo,
                                      List<ScheduleInfo> schedules) {
        return new EmployeeInfo().builder()
                .shortInfo(mapDataToShortInfo(employee))
                .phoneNr(employee.getPhoneNr())
                .pesel(employee.getPesel())
                .accountNr(employee.getAccountNr())
                .salary(employee.getSalary())
                .active(employee.isActive())
                .employmentDate(employee.getEmploymentDate())
                .dismissalDate(employee.getDismissalDate())
                .address(AddressMapper.mapDataToObject(employee.getAddress()))
                .restaurantInfo(restaurantInfo)
                .schedulesInfo(schedules)
                .build();
    }

    public EmployeeEntity mapObjectToData(Employee employee, AddressEntity address,
                                          WorkstationEntity workstation) {
        return new EmployeeEntity().builder()
                .restaurantId(employee.getRestaurantId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .pesel(employee.getPesel())
                .phoneNr(employee.getPhoneNr())
                .accountNr(employee.getAccountNr())
                .salary(employee.getSalary())
                .active(employee.getActive())
                .employmentDate(employee.getEmploymentDate())
                .dismissalDate(employee.getDismissalDate())
                .address(address)
                .workstation(workstation)
                .build();
    }
}
