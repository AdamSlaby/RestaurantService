package pl.restaurant.employeeservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeShortInfo {
    private Long id;
    private String name;
    private String surname;
    private Integer workstationId;
}
