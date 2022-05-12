package pl.restaurant.newsservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import pl.restaurant.newsservice.api.response.NewsInfo;
import pl.restaurant.newsservice.api.response.NewsShortInfo;
import pl.restaurant.newsservice.data.entity.NewsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepo extends JpaRepository<NewsEntity, Long> {

    @Query("select new pl.restaurant.newsservice.api.response." +
            "NewsInfo(n.newsId, n.imageUrl, n.title, n.content, n.publishedDate) " +
            "from NewsEntity n " +
            "where n.newsId = :id")
    Optional<NewsInfo> getNewsInfoById(@PathVariable("id") Long newsId);

    @Query("select n.employeeId from NewsEntity n where n.newsId = :id")
    Optional<Long> getNewsEmployeeId(@PathVariable("id") Long newsId);

    @Query("select new pl.restaurant.newsservice.api.response." +
            "NewsInfo(n.newsId, n.imageUrl, n.title, n.content, n.publishedDate) " +
            "from NewsEntity n")
    Page<NewsInfo> getNewsList(Pageable pageable);

    @Query("select new pl.restaurant.newsservice.api.response" +
            ".NewsShortInfo(n.newsId, n.employeeId, n.title, n.publishedDate) " +
            "from NewsEntity n " +
            "where (:nId is null or n.newsId = :nId) and " +
            "(:eId is null or n.employeeId = :eId) and " +
            "(:date is null or n.publishedDate = :date) and " +
            "(:title is null or n.title like %:title%)")
    Page<NewsShortInfo> getNews(@Param("nId") Long newsId,
                                @Param("eId") Long employeeId,
                                @Param("title") String title,
                                @Param("date") LocalDate date,
                                Pageable pageable);
}
