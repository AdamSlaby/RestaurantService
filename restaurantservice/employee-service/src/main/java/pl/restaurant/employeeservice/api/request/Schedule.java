package pl.restaurant.employeeservice.api.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @NotNull(message = "Identyfikator pracownika nie może być pusty")
    @Min(value = 1, message = "Idenyfikator pracownika nie może być liczbą ujemną")
    private Long employeeId;

    @NotNull(message = "Data i godzina rozpoczęcia pracy nie może być pusta")
    private LocalDateTime startShift;

    @NotNull(message = "Data i godzina zakończenia pracy nie może być pusta")
    private LocalDateTime endShift;
}
