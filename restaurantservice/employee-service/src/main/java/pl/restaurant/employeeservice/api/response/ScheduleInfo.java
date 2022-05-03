package pl.restaurant.employeeservice.api.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInfo {
    private Long id;
    private LocalDateTime startShift;
    private LocalDateTime endShift;
}
