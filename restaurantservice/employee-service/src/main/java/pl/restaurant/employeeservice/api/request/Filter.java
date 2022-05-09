package pl.restaurant.employeeservice.api.request;

import lombok.*;

import javax.validation.Valid;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private Long restaurantId;
    private Long employeeId;
    private Boolean active;
    private String surname;
    private Integer workstationId;
    @Valid
    private SortEvent sortEvent;
    private int pageNr;
}
