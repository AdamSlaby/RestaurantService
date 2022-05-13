package pl.restaurant.newsservice.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsFilters {
    private Long newsId;
    private Long employeeId;
    private String title;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    @Valid
    private SortEvent sortEvent;
    private int pageNr;
}
