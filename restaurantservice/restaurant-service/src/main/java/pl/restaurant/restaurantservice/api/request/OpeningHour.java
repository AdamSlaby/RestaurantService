package pl.restaurant.restaurantservice.api.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHour {
    private Long hourId;

    @NotNull(message = "Numer dnia tygodnia jest wymagany")
    private Integer weekDayNr;

    @NotNull(message = "Godzina otwrcia jest wymagana")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromHour;

    @NotNull(message = "Godzina zamknięcia jest wymagana")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toHour;
}
