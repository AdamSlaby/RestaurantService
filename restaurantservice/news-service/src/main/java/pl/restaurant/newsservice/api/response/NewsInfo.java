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
public class NewsInfo {
    private Long newsId;
    private String imageUrl;
    private String title;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
}
