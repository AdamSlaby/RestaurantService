package pl.restaurant.newsservice.business.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
import pl.restaurant.newsservice.business.utility.FileUtility;
import pl.restaurant.newsservice.business.utility.ImageValidator;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class NewsServiceImpl implements NewsService {
    private static final int CLIENT_PAGE_AMOUNT = 5;
    private static final int USER_PAGE_AMOUNT = 10;
    public static final String IMAGE_URL = "http://localhost:9000/news/image/";

    @Value("${app.file.path}")
    private String path;
    private final NewsRepo newsRepo;
    private final EmployeeServiceClient serviceClient;

    public NewsServiceImpl(NewsRepo newsRepo, EmployeeServiceClient serviceClient) {
        this.newsRepo = newsRepo;
        this.serviceClient = serviceClient;
    }

    @Override
    public NewsInfo getNewsInfo(Long newsId) {
        return newsRepo.getNewsInfoById(newsId).orElseThrow(NewsNotFoundException::new);
    }

    @Override
    public List<NewsInfo> getNews(Integer pageNr) {
        Pageable pageable = PageRequest.of(pageNr, CLIENT_PAGE_AMOUNT,
                Sort.by(Sort.Direction.DESC, "publishedDate"));
        List<NewsInfo> content = newsRepo.getNewsList(pageable).getContent();
        content.forEach(el -> {
                    int index = el.getContent().indexOf(".", 200);
                    if (index >= 200)
                        el.setContent(el.getContent().substring(0, index + 1));
                });
        return content;
    }

    @Override
    public News getNewsDetails(Long newsId, HttpServletRequest request) {
        NewsInfo info = newsRepo.getNewsInfoById(newsId).orElseThrow(NewsNotFoundException::new);
        String fullName = serviceClient.getEmployeeFullName(request.getHeader("Authorization"), newsRepo
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
    @Transactional
    public void createNews(NewsRequest news) {
        validateImage(news.getImage());
        if (!serviceClient.isAdminEmployeeExist(news.getEmployeeId()))
            throw new EmployeeNotFoundException();
        String filename = FileUtility.fileUpload(news.getImage(), path);
        newsRepo.save(new NewsEntity().builder()
                .employeeId(news.getEmployeeId())
                .title(news.getTitle())
                .content(news.getContent())
                .imageUrl(IMAGE_URL + filename)
                .publishedDate(LocalDate.now())
                .build());
    }

    @Override
    @Transactional
    public void updateNews(Long newsId, NewsRequest news) {
        if (!serviceClient.isAdminEmployeeExist(news.getEmployeeId()))
            throw new EmployeeNotFoundException();
        NewsEntity newsEntity = newsRepo.findById(newsId).orElseThrow(NewsNotFoundException::new);
        if (news.getImage() != null) {
            validateImage(news.getImage());
            String filename = FileUtility.fileUpload(news.getImage(), path);
            String[] parts = newsEntity.getImageUrl().split("/");
            FileUtility.deleteFile(parts[parts.length - 1], path);
            newsEntity.setImageUrl(IMAGE_URL + filename);
        }
        newsEntity.setTitle(news.getTitle());
        newsEntity.setContent(news.getContent());
        newsRepo.save(newsEntity);
    }

    @Override
    @Transactional
    public void removeNews(Long newsId) {
        NewsEntity newsEntity = newsRepo.findById(newsId).orElseThrow(NewsNotFoundException::new);
        String[] parts = newsEntity.getImageUrl().split("/");
        FileUtility.deleteFile(parts[parts.length - 1], path);
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
