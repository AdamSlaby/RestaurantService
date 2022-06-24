package pl.restaurant.orderservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.orderservice.api.response.chart.Chart;
import pl.restaurant.orderservice.api.response.chart.ChartData;
import pl.restaurant.orderservice.data.entity.*;
import pl.restaurant.orderservice.data.repository.OnlineOrderMealRepo;
import pl.restaurant.orderservice.data.repository.OnlineOrderRepo;
import pl.restaurant.orderservice.data.repository.RestaurantOrderMealRepo;
import pl.restaurant.orderservice.data.repository.RestaurantOrderRepo;
import pl.restaurant.orderservice.integration.MysqlTest;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StatisticControllerTest extends MysqlTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OnlineOrderRepo onlineOrderRepo;

    @Autowired
    private OnlineOrderMealRepo onlineOrderMealRepo;

    @Autowired
    private RestaurantOrderRepo restaurantOrderRepo;

    @Autowired
    private RestaurantOrderMealRepo restaurantOrderMealRepo;

    @LocalServerPort
    private int port;

    private URL url;

    @BeforeEach
    public void setUp() throws Exception {
        //online order
        OnlineOrderEntity online = onlineOrderRepo.save(getOnlineOrder(1L, LocalDateTime.now()));
        onlineOrderMealRepo.save(getOnlineMeal(online.getOrderId(), 1, online));
        OnlineOrderEntity online2 = onlineOrderRepo.save(getOnlineOrder(1L, LocalDateTime.now().minusDays(1)));
        onlineOrderMealRepo.save(getOnlineMeal(online2.getOrderId(), 2, online2));
        OnlineOrderEntity online3 = onlineOrderRepo.save(getOnlineOrder(2L, LocalDateTime.now()));
        onlineOrderMealRepo.save(getOnlineMeal(online3.getOrderId(), 5, online3));
        //restaurant order
        RestaurantOrderEntity rest1 = restaurantOrderRepo.save(getRestaurantOrder(1L, LocalDateTime.now()));
        restaurantOrderMealRepo.save(getRestaurantMeal(rest1.getOrderId(), 3, rest1));
        RestaurantOrderEntity rest2 = restaurantOrderRepo.save(getRestaurantOrder(1L, LocalDateTime.now().minusDays(1)));
        restaurantOrderMealRepo.save(getRestaurantMeal(rest2.getOrderId(), 4, rest2));
        RestaurantOrderEntity rest3 = restaurantOrderRepo.save(getRestaurantOrder(2L, LocalDateTime.now().minusDays(1)));
        restaurantOrderMealRepo.save(getRestaurantMeal(rest3.getOrderId(), 6, rest3));
        //restaurant active order
        RestaurantOrderEntity restaurantOrder = getRestaurantOrder(2L, LocalDateTime.now());
        restaurantOrder.setDeliveryDate(null);
        RestaurantOrderEntity rest4 = restaurantOrderRepo.save(restaurantOrder);
        restaurantOrderMealRepo.save(getRestaurantMeal(rest4.getOrderId(), 6, rest4));
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        onlineOrderRepo.deleteAll();
        restaurantOrderRepo.deleteAll();
    }

    @Test
    public void getTodayIncomeSuccess() {
        //given
        Long restaurantId = 1L;
        BigDecimal income = BigDecimal.valueOf(40);
        //when
        ResponseEntity<BigDecimal> response = restTemplate
                .getForEntity(this.url + "statistic/income?rId=" + restaurantId, BigDecimal.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().compareTo(income)).isEqualTo(0);
    }

    @Test
    public void getTodayIncomeFromAllRestaurantsSuccess() {
        //given
        BigDecimal income = BigDecimal.valueOf(60);
        //when
        ResponseEntity<BigDecimal> response = restTemplate
                .getForEntity(this.url + "statistic/income", BigDecimal.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().compareTo(income)).isEqualTo(0);
    }

    @Test
    public void getTodayIncomeSuccessRestaurantIdDoesNotExist() {
        //given
        Long restaurantId = -1L;
        //when
        ResponseEntity<BigDecimal> response = restTemplate
                .getForEntity(this.url + "statistic/income?rId=" + restaurantId, BigDecimal.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void getTodayDeliveredOrdersAmountSuccess() {
        //given
        Long restaurantId = 1L;
        int orders = 2;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/orders?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getTodayDeliveredOrdersAmountFromAllRestaurantsSuccess() {
        //given
        int orders = 3;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/orders", Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getTodayDeliveredOrdersAmountSuccessRestaurantIdDoesNotExist() {
        //given
        Long restaurantId = -1L;
        int orders = 0;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/orders?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getTodayDeliveredMealsAmountSuccess() {
        //given
        Long restaurantId = 1L;
        int meals = 2;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/meals?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(meals);
    }

    @Test
    public void getTodayDeliveredMealsAmountFromAllRestaurantsSuccess() {
        //given
        int meals = 3;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/meals", Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(meals);
    }

    @Test
    public void getTodayDeliveredMealsAmountSuccessRestaurantIdDoesNotExist() {
        //given
        Long restaurantId = -1L;
        int meals = 0;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/meals?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(meals);
    }

    @Test
    public void getActiveOrdersAmountSuccess() {
        //given
        Long restaurantId = 1L;
        int orders = 0;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/active?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getActiveOrdersAmountFromAllRestaurantsSuccess() {
        //given
        int orders = 1;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/active", Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getActiveOrdersAmountSuccessRestaurantIdDoesNotExist() {
        //given
        Long restaurantId = -1L;
        int orders = 0;
        //when
        ResponseEntity<Integer> response = restTemplate
                .getForEntity(this.url + "statistic/active?rId=" + restaurantId, Integer.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    public void getOrderAmountFromHoursSuccess() {
        //given
        int hour = LocalDateTime.now().getHour();
        Long restaurantId = 1L;
        int orders = 2;
        //when
        ResponseEntity<Chart> response = restTemplate
                .getForEntity(this.url + "statistic/hours?rId=" + restaurantId, Chart.class);
        System.out.println(response.getBody());
        ChartData chartData = response.getBody().getSeries().stream().filter(el -> el.getName().equals(hour + ":00"))
                .collect(Collectors.toList()).get(0);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSeries()).hasSize(14);
        assertThat(chartData.getValue()).isEqualTo(orders);
    }

    @Test
    public void getOrderAmountFromHoursFromAllRestaurantsSuccess() {
        //given
        int hour = LocalDateTime.now().getHour();
        int orders = 4;
        //when
        ResponseEntity<Chart> response = restTemplate
                .getForEntity(this.url + "statistic/hours", Chart.class);
        System.out.println(response.getBody());
        ChartData chartData = response.getBody().getSeries().stream().filter(el -> el.getName().equals(hour + ":00"))
                .collect(Collectors.toList()).get(0);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSeries()).hasSize(14);
        assertThat(chartData.getValue()).isEqualTo(orders);
    }

    @Test
    public void getOrderAmountFromHoursSuccessRestaurantIdDoesNotExist() {
        //given
        Long restaurantId = -1L;
        //when
        ResponseEntity<Chart> response = restTemplate
                .getForEntity(this.url + "statistic/hours?rId=" + restaurantId, Chart.class);
        System.out.println(response.getBody());
        List<ChartData> list = response.getBody().getSeries().stream().filter(el -> el.getValue() > 0)
                .collect(Collectors.toList());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list).hasSize(0);
    }

    private OnlineOrderEntity getOnlineOrder(Long restaurantId, LocalDateTime from) {
        EasyRandom generator = new EasyRandom(getParameters());
        OnlineOrderEntity order = generator.nextObject(OnlineOrderEntity.class);
        order.setRestaurantId(restaurantId);
        order.setOrderDate(from);
        order.setAddress(getAddress());
        order.setPrice(BigDecimal.valueOf(20));
        order.setMeals(null);
        return order;
    }

    private OnlineOrderMealEntity getOnlineMeal(Long orderId, Integer mealId, OnlineOrderEntity order) {
        return new OnlineOrderMealEntity().builder()
                .id(new OrderMealId(orderId, mealId))
                .order(order)
                .mealName("Pomidorowa")
                .price(BigDecimal.valueOf(20))
                .quantity(1)
                .build();
    }

    private RestaurantOrderEntity getRestaurantOrder(Long restaurantId, LocalDateTime date) {
        EasyRandom generator = new EasyRandom(getParameters());
        RestaurantOrderEntity restaurantOrder = generator.nextObject(RestaurantOrderEntity.class);
        restaurantOrder.setRestaurantId(restaurantId);
        restaurantOrder.setPrice(BigDecimal.valueOf(20));
        restaurantOrder.setTableId(1L);
        restaurantOrder.setOrderDate(date);
        restaurantOrder.setDeliveryDate(LocalDateTime.now());
        restaurantOrder.setMeals(null);
        return restaurantOrder;
    }

    private RestaurantOrderMealEntity getRestaurantMeal(Long orderId, Integer mealId, RestaurantOrderEntity order) {
        return new RestaurantOrderMealEntity().builder()
                .id(new OrderMealId(orderId, mealId))
                .order(order)
                .mealName("Pomidorowa")
                .price(BigDecimal.valueOf(20))
                .quantity(1)
                .build();
    }

    private AddressEntity getAddress() {
        AddressEntity addressEntity = new EasyRandom(getParameters()).nextObject(AddressEntity.class);
        addressEntity.setOrder(null);
        addressEntity.setAddressId(null);
        return addressEntity;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
