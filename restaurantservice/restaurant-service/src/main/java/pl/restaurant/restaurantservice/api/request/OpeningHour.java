package pl.restaurant.restaurantservice.api.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHour {
    private Long hourId;

    @NotNull(message = "Numer dnia tygodnia jest wymagany")
    private Integer weekDayNr;

    @NotNull(message = "Godzina otwrcia jest wymagana")
    private LocalTime fromHour;

    @NotNull(message = "Godzina zamknięcia jest wymagana")
    private LocalTime toHour;
}
