package pl.restaurant.newsservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import pl.restaurant.newsservice.api.response.News;
import pl.restaurant.newsservice.business.service.EmployeeServiceClient;
import pl.restaurant.newsservice.data.entity.NewsEntity;
import pl.restaurant.newsservice.data.repository.NewsRepo;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
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
        newsRepo.save(getNews(2L, "Zamkniecie restaurcji", LocalDate.now().plusDays(1)));
        newsRepo.save(getNews(1L, "Nowe danie w menu", LocalDate.now()));
        newsRepo.save(getNews(3L, "Nowe danie w menu", LocalDate.now().minusDays(1)));
        this.url = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void getNewsDetailsSuccess() {
        //given
        String fullname = "Maciek Kowalski";
        //when
        when(client.getEmployeeFullName(any(), any(Long.class))).thenReturn(fullname);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        ResponseEntity<News> response = restTemplate
                .getForEntity(this.url + "details/" + this.newsId, News.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getInfo().getNewsId()).isEqualTo(this.newsId);
        assertThat(response.getBody().getEmployeeName()).isEqualTo(fullname);
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
