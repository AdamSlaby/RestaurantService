package pl.restaurant.newsservice.business.service;

import pl.restaurant.newsservice.api.request.NewsFilters;
import pl.restaurant.newsservice.api.request.NewsRequest;
import pl.restaurant.newsservice.api.response.News;
import pl.restaurant.newsservice.api.response.NewsInfo;
import pl.restaurant.newsservice.api.response.NewsListView;

import java.util.List;

public interface NewsService {
    NewsInfo getNewsInfo(Long newsId);
    List<NewsInfo> getNews(Integer pageNr);
    News getNewsDetails(Long newsId);
    NewsListView getNewsList(NewsFilters newsFilters);
    void createNews(NewsRequest news);
    void updateNews(Long newsId, NewsRequest news);
    void removeNews(Long newsId);
}
