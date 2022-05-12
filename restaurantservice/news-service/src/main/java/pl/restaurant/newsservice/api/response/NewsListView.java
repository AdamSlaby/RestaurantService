package pl.restaurant.newsservice.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsListView {
    private Long totalElements;
    private List<NewsShortInfo> news;
}
