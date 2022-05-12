package pl.restaurant.newsservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {
    private NewsInfo info;
    private String employeeName;
}
