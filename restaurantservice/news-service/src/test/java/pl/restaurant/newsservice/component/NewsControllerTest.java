package pl.restaurant.newsservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.newsservice.api.response.News;
import pl.restaurant.newsservice.api.response.NewsInfo;
import pl.restaurant.newsservice.business.service.EmployeeServiceClient;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import java.net.URL;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NewsControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NewsRepo newsRepo;

    @MockBean
    private EmployeeServiceClient client;

    @LocalServerPort
    private int port;

    private URL url;
    private Long newsId;

    @BeforeEach
    public void setUp() throws Exception {
        this.newsId = newsRepo.save(getNews(1L, "Otwarcie restaurcji", LocalDate.now())).getNewsId();
        newsRepo.save(getNews(2L, "Zamkniecie restaurcji", LocalDate.now()));
        newsRepo.save(getNews(1L, "Nowe danie w menu", LocalDate.now()));
        newsRepo.save(getNews(3L, "Nowe danie w menu", LocalDate.now()));
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        newsRepo.deleteAll();
    }

    @Test
    public void getNewsDetailsSuccess() {
        //given
        String fullname = "Maciek Kowalski";
        //when
        when(client.getEmployeeFullName(any(), any(Long.class))).thenReturn(fullname);
        ResponseEntity<News> response = restTemplate
                .getForEntity(this.url + "details/" + this.newsId, News.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getInfo().getNewsId()).isEqualTo(this.newsId);
        assertThat(response.getBody().getEmployeeName()).isEqualTo(fullname);
    }

    @Test
    public void getNewsDetailsFailure() {
        //given
        //when
        ResponseEntity<News> response = restTemplate
                .getForEntity(this.url + "details/" + 0, News.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getNewsListSuccess() {
        //given
        int pageNr = 0;
        //when
        ResponseEntity<NewsInfo[]> response = restTemplate
                .getForEntity(this.url + "list/info?pageNr=" + pageNr, NewsInfo[].class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(4);
        assertThat(response.getBody()[0].getContent().length()).isGreaterThan(0);
    }

    @Test
    public void getNewsListFailure() {
        //given
        int pageNr = -1;
        //when
        ResponseEntity<String> response = restTemplate
                .getForEntity(this.url + "list/info?pageNr=" + pageNr, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
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
