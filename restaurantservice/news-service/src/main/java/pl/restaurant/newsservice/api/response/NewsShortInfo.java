package pl.restaurant.newsservice.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsShortInfo {
    private Long id;
    private Long employeeId;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
}
