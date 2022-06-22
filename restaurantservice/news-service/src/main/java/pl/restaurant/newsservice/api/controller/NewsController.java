package pl.restaurant.newsservice.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.restaurant.newsservice.api.request.NewsFilters;
import pl.restaurant.newsservice.api.request.NewsRequest;
import pl.restaurant.newsservice.api.response.News;
import pl.restaurant.newsservice.api.response.NewsInfo;
import pl.restaurant.newsservice.api.response.NewsListView;
import pl.restaurant.newsservice.business.service.NewsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@CrossOrigin
@Validated
@Log4j2
@AllArgsConstructor
public class NewsController {
    private NewsService newsService;

    @GetMapping("/info/{id}")
    public NewsInfo getNewsInfo(@PathVariable("id") Long newsId) {
        return newsService.getNewsInfo(newsId);
    }

    @GetMapping("/list/info")
    public List<NewsInfo> getNews(@Valid @RequestParam("pageNr") @Min(value = 0, message = "Page number cannot be negative number")
                                      Integer pageNr) {
        return newsService.getNews(pageNr);
    }

    @GetMapping("/details/{id}")
    public News getNewsDetails(@PathVariable("id") Long newsId, HttpServletRequest request) {
        return newsService.getNewsDetails(newsId, request);
    }

    @PostMapping("/list")
    public NewsListView getNewsList(@RequestBody @Valid NewsFilters newsFilters) {
        return newsService.getNewsList(newsFilters);
    }

    @PostMapping()
    public void createNews(@ModelAttribute @Valid NewsRequest news) {
        newsService.createNews(news);
    }

    @PatchMapping("/{id}")
    public void updateNews(@PathVariable("id") Long newsId, @ModelAttribute @Valid NewsRequest news) {
        newsService.updateNews(newsId, news);
    }

    @DeleteMapping("/{id}")
    public void removeNews(@PathVariable("id") Long newsId) {
        newsService.removeNews(newsId);
    }
}
