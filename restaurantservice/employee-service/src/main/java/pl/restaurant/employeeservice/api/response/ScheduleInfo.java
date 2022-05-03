package pl.restaurant.employeeservice.api.response;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInfo {
    @NotNull(message = "Identyfikator harmonogrmu nie może być pusty")
    @Min(value = 1, message = "Identyfikator harmonogramu nie może być liczbą ujemną")
    private Long id;

    @NotNull(message = "Data i godzina rozpoczęcia pracy nie może być pusta")
    private LocalDateTime startShift;

    @NotNull(message = "Data i godzina zakończenia pracy nie może być pusta")
    private LocalDateTime endShift;
}
