package pl.restaurant.newsservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsShortInfo {
    private Long id;
    private Long employeeId;
    private String title;
    private LocalDateTime date;
}
