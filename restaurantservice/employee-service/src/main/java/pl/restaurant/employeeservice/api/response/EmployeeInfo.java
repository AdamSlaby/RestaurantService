package pl.restaurant.employeeservice.api.response;

import lombok.*;
import pl.restaurant.employeeservice.api.request.Address;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfo {
    private EmployeeShortInfo shortInfo;
    private String phoneNr;
    private String pesel;
    private String accountNr;
    private BigDecimal salary;
    private boolean active;
    private LocalDate employmentDate;
    private LocalDate dismissalDate;
    private Address address;
    private RestaurantShortInfo restaurantInfo;
    private List<ScheduleInfo> schedulesInfo;
}
