package pl.restaurant.newsservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.newsservice.api.request.NewsFilters;
import pl.restaurant.newsservice.api.request.NewsRequest;
import pl.restaurant.newsservice.api.request.SortDirection;
import pl.restaurant.newsservice.api.response.News;
import pl.restaurant.newsservice.api.response.NewsInfo;
import pl.restaurant.newsservice.api.response.NewsListView;
import pl.restaurant.newsservice.api.response.NewsShortInfo;
import pl.restaurant.newsservice.business.exception.ColumnNotFoundException;
import pl.restaurant.newsservice.business.exception.EmployeeNotFoundException;
import pl.restaurant.newsservice.business.exception.NewsNotFoundException;
import pl.restaurant.newsservice.business.utility.ImageValidator;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    private static final int CLIENT_PAGE_AMOUNT = 5;
    private static final int USER_PAGE_AMOUNT = 10;
    private NewsRepo newsRepo;
    private EmployeeServiceClient serviceClient;
    @Override
    public NewsInfo getNewsInfo(Long newsId) {
        return newsRepo.getNewsInfoById(newsId).orElseThrow(NewsNotFoundException::new);
    }

    @Override
    public List<NewsInfo> getNews(Integer pageNr) {
        Pageable pageable = PageRequest.of(pageNr, CLIENT_PAGE_AMOUNT, Sort.by(Sort.Direction.DESC, "publishedDate"));
        return newsRepo.getNewsList(pageable).getContent();
    }

    @Override
    public News getNewsDetails(Long newsId) {
        NewsInfo info = newsRepo.getNewsInfoById(newsId).orElseThrow(NewsNotFoundException::new);
        String fullName = serviceClient.getEmployeeFullName(newsRepo
                .getNewsEmployeeId(newsId)
                .orElseThrow(NewsNotFoundException::new));
        return new News().builder()
                .employeeName(fullName)
                .info(info)
                .build();
    }

    @Override
    public NewsListView getNewsList(NewsFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<NewsShortInfo> page = newsRepo.getNews(filters.getNewsId(), filters.getEmployeeId(),
                filters.getTitle(), filters.getDate(), pageable);
        return new NewsListView().builder()
                .totalElements(page.getTotalElements())
                .news(page.getContent())
                .build();
    }

    @Override
    public void createNews(NewsRequest news) {
        validateImage(news.getImage());
        if (!serviceClient.isAdminEmployeeExist(news.getEmployeeId()))
            throw new EmployeeNotFoundException();

    }

    @Override
    public void updateNews(Long newsId, NewsRequest news) {

    }

    @Override
    @Transactional
    public void removeNews(Long newsId) {
        newsRepo.deleteById(newsId);
    }

    private Pageable mapSortEventToPageable(NewsFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), USER_PAGE_AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                NewsEntity.class.getDeclaredField(column);
                if (filters.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filters.getPageNr(), USER_PAGE_AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filters.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filters.getPageNr(), USER_PAGE_AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filters.getPageNr(), USER_PAGE_AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }

    private void validateImage(MultipartFile image) {
        ImageValidator.validateImageNull(image);
        ImageValidator.validateImageExtension(image);
        ImageValidator.validateImageSize(image);
    }
}
