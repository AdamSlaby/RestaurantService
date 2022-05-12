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
public class NewsInfo {
    private Long newsId;
    private String imageUrl;
    private String title;
    private String content;
    private LocalDateTime date;
}
