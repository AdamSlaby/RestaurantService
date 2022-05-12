package pl.restaurant.newsservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    private Long employeeId;

    @Column(length = 201, nullable = false)
    private String title;

    @Column(length = 2501, nullable = false)
    private String content;

    @Column(length = 101, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime publishedDate;
}
