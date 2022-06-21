package pl.restaurant.newsservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.newsservice.api.request.NewsRequest;
import pl.restaurant.newsservice.business.exception.EmployeeNotFoundException;
import pl.restaurant.newsservice.business.exception.InvalidImageSizeException;
import pl.restaurant.newsservice.business.exception.NewsNotFoundException;
import pl.restaurant.newsservice.business.service.EmployeeServiceClient;
import pl.restaurant.newsservice.business.service.NewsService;
import pl.restaurant.newsservice.business.service.NewsServiceImpl;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class NewsServiceTest {
    @Autowired
    private NewsService newsService;

    @Value("${app.file.path}")
    String path;

    @Autowired
    private NewsRepo newsRepo;

    @MockBean
    private EmployeeServiceClient client;

    @AfterEach
    public void clear() {
        List<NewsEntity> all = newsRepo.findAll();
        if (all.size() > 0) {
            String imageName = all.get(0).getImageUrl().split(NewsServiceImpl.IMAGE_URL)[1];
            new File(path + imageName).deleteOnExit();
        }
        newsRepo.deleteAll();
    }

    @Test
    public void createNewsSuccess() {
        //given
        String filename = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(filename, filename, null, new byte[10]);
        NewsRequest newsRequest = new NewsRequest(1L, file, "Test", "Test");
        //when
        when(client.isAdminEmployeeExist(any(Long.class))).thenReturn(true);
        newsService.createNews(newsRequest);
        NewsEntity newsEntity = newsRepo.findAll().get(0);
        //then
        assertThat(newsEntity.getEmployeeId()).isEqualTo(1L);
        assertTrue(newsEntity.getImageUrl().contains(NewsServiceImpl.IMAGE_URL));
        assertThat(newsEntity.getPublishedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void createNewsFailureEmployeeIsNotAdmin() {
        //given
        String filename = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(filename, filename, null, new byte[10]);
        NewsRequest newsRequest = new NewsRequest(1L, file, "Test", "Test");
        //when
        when(client.isAdminEmployeeExist(any(Long.class))).thenReturn(false);
        //then
        assertThrows(EmployeeNotFoundException.class, () -> newsService.createNews(newsRequest));
    }

    @Test
    public void createNewsFailureFileTooLarge() {
        //given
        String filename = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(filename, filename, null, new byte[5000001]);
        NewsRequest newsRequest = new NewsRequest(1L, file, "Test", "Test");
        //when
        when(client.isAdminEmployeeExist(any(Long.class))).thenReturn(true);
        //then
        assertThrows(InvalidImageSizeException.class, () -> newsService.createNews(newsRequest));
    }

    @Test
    public void removeNewsSuccess() {
        //given
        String filename = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(filename, filename, null, new byte[5]);
        NewsRequest newsRequest = new NewsRequest(1L, file, "Test", "Test");
        //when
        when(client.isAdminEmployeeExist(any(Long.class))).thenReturn(true);
        newsService.createNews(newsRequest);
        NewsEntity newsEntity = newsRepo.findAll().get(0);
        Long newsId = newsEntity.getNewsId();
        String imageName = newsEntity.getImageUrl().split(NewsServiceImpl.IMAGE_URL)[1];
        newsService.removeNews(newsId);
        List<NewsEntity> all = newsRepo.findAll();
        //then
        assertThat(all).hasSize(0);
        assertFalse(new File(path + imageName).exists());
    }

    @Test
    public void removeNewsFailure() {
        //given
        //when
        //then
        assertThrows(NewsNotFoundException.class, () -> newsService.removeNews(-1L));
    }
}
