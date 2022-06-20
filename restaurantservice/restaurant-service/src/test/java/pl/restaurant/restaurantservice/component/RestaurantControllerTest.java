package pl.restaurant.restaurantservice.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.restaurantservice.api.controller.RestaurantController;
import pl.restaurant.restaurantservice.api.mapper.AddressMapper;
import pl.restaurant.restaurantservice.api.mapper.RestaurantMapper;
import pl.restaurant.restaurantservice.api.request.Address;
import pl.restaurant.restaurantservice.api.request.OpeningHour;
import pl.restaurant.restaurantservice.api.request.Restaurant;
import pl.restaurant.restaurantservice.api.response.RestaurantInfo;
import pl.restaurant.restaurantservice.business.service.RestaurantService;
import pl.restaurant.restaurantservice.data.entity.AddressEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.repository.AddressRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestaurantControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private RestaurantRepo repo;

    @Autowired
    private AddressRepo addressRepo;

    private URL base;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        repo.deleteAll();
    }

    @Test
    public void addRestaurantSuccess() {
        //given
        Restaurant restaurant = getRestaurant();
        //when
        HttpEntity<Restaurant> request = new HttpEntity<>(restaurant);
        ResponseEntity<Void> response = template.postForEntity(base + "new", request, Void.class);
        List<RestaurantEntity> all = repo.findAll();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getEmail()).isEqualTo(restaurant.getEmail());
    }

    @Test
    public void addRestaurantFailure() {
        //given
        Restaurant restaurant = getRestaurant();
        restaurant.getAddress().setCity("");
        //when
        HttpEntity<Restaurant> request = new HttpEntity<>(restaurant);
        ResponseEntity<Void> response = template.postForEntity(base + "new", request, Void.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getRestaurantInfoSuccess() {
        //given
        Restaurant restaurant = getRestaurant();
        AddressEntity address = AddressMapper.mapObjectToData(restaurant.getAddress());
        Long restaurantId = repo.save(RestaurantMapper.mapObjectToData(restaurant, address)).getRestaurantId();
        //when
        ResponseEntity<RestaurantInfo> response = template
                .getForEntity(base + "info/" + restaurantId, RestaurantInfo.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo(restaurant.getEmail());
    }

    @Test
    public void getRestaurantInfoWhichNotExist() {
        //given
        Restaurant restaurant = getRestaurant();
        AddressEntity address = AddressMapper.mapObjectToData(restaurant.getAddress());
        Long restaurantId = repo.save(RestaurantMapper.mapObjectToData(restaurant, address)).getRestaurantId();
        //when
        ResponseEntity<RestaurantInfo> response = template
                .getForEntity(base + "info/" + 0, RestaurantInfo.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Address getAddress() {
        return new Address().builder()
                .city("Kielce")
                .street("Warszawska")
                .houseNr("100")
                .flatNr("20")
                .postcode("25-300")
                .build();
    }

    private List<OpeningHour> getOpeningHours() {
        List<OpeningHour> hours = new ArrayList<>();
        for (int i = 1; i <= 7; i++)
            hours.add(getOpeningHour(i));
        return hours;
    }

    private OpeningHour getOpeningHour(int weekdayNr) {
        return new OpeningHour().builder()
                .weekDayNr(weekdayNr)
                .fromHour(LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                .toHour(LocalDateTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0))
                .build();
    }

    private Restaurant getRestaurant() {
        return new Restaurant().builder()
                .address(getAddress())
                .email("adsa2233@interia.pl")
                .phoneNr("303 303 303")
                .deliveryFee(BigDecimal.valueOf(5))
                .minimalDeliveryPrice(BigDecimal.valueOf(30))
                .openingHours(getOpeningHours())
                .tables(new ArrayList<>())
                .build();
    }
}
