package pl.restaurant.employeeservice.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListView {
    private Long totalElements;
    private List<EmployeeShortInfo> employees;
}
