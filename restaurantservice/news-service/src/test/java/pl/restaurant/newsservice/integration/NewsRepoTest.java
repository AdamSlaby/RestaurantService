package pl.restaurant.newsservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.restaurant.newsservice.api.response.NewsShortInfo;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class NewsRepoTest {
    @Autowired
    private NewsRepo newsRepo;

    private Long newsId;

    @BeforeEach
    public void fillDatabase() {
        this.newsId = newsRepo.save(getNews(1L, "Otwarcie restaurcji", LocalDate.now())).getNewsId();
        newsRepo.save(getNews(2L, "Zamkniecie restaurcji", LocalDate.now().plusDays(1)));
        newsRepo.save(getNews(1L, "Nowe danie w menu", LocalDate.now()));
        newsRepo.save(getNews(3L, "Nowe danie w menu", LocalDate.now().minusDays(1)));
    }

    @Test
    public void getNewsByNewsIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "Otwarcie restaurcji";
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(this.newsId, null, null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo(title);
        assertThat(page.getContent().get(0).getId()).isEqualTo(this.newsId);
    }

    @Test
    public void getNewsByEmployeeIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long employeeId = 1L;
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(null, employeeId, null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getEmployeeId()).isEqualTo(employeeId);
    }

    @Test
    public void getNewsByTitleSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "restaurcji";
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(null, null, title, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertTrue(page.getContent().get(0).getTitle().contains(title));
        assertTrue(page.getContent().get(1).getTitle().contains(title));
    }

    @Test
    public void getNewsByDateSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate now = LocalDate.now();
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(null, null, null, now, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getDate()).isEqualTo(now);
        assertThat(page.getContent().get(1).getDate()).isEqualTo(now);
    }

    @Test
    public void getNewsByTwoParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long employeeId = 1L;
        LocalDate now = LocalDate.now();
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(null, employeeId, null, now, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getEmployeeId()).isEqualTo(employeeId);
        assertThat(page.getContent().get(0).getDate()).isEqualTo(now);
    }

    @Test
    public void getNewsByAllParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long employeeId = 1L;
        String title = "restaurcji";
        LocalDate now = LocalDate.now();
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(this.newsId, employeeId, title, now, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(this.newsId);
        assertThat(page.getContent().get(0).getEmployeeId()).isEqualTo(employeeId);
        assertTrue(page.getContent().get(0).getTitle().contains(title));
        assertThat(page.getContent().get(0).getDate()).isEqualTo(now);
    }

    @Test
    public void getNewsByAllNullParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<NewsShortInfo> page = newsRepo.getNews(null, null, null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(4);
    }

    private NewsEntity getNews(Long employeeId, String title, LocalDate date) {
        EasyRandom random = new EasyRandom(getParameters());
        NewsEntity news = random.nextObject(NewsEntity.class);
        news.setEmployeeId(employeeId);
        news.setTitle(title);
        news.setPublishedDate(date);
        return news;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(10, 10);
        parameters.collectionSizeRange(1, 3);
        return parameters;
    }
}
