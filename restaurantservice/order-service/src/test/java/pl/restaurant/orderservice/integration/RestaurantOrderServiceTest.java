package pl.restaurant.orderservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import pl.restaurant.orderservice.api.mapper.RestaurantOrderMapper;
import pl.restaurant.orderservice.api.request.KeycloakResponse;
import pl.restaurant.orderservice.api.request.Order;
import pl.restaurant.orderservice.api.request.RestaurantOrder;
import pl.restaurant.orderservice.business.exception.CannotUpdateOrderException;
import pl.restaurant.orderservice.business.exception.InvalidOrderException;
import pl.restaurant.orderservice.business.exception.OrderNotFoundException;
import pl.restaurant.orderservice.business.exception.TableNotFoundException;
import pl.restaurant.orderservice.business.service.authentication.LoginService;
import pl.restaurant.orderservice.business.service.client.MenuServiceClient;
import pl.restaurant.orderservice.business.service.client.RestaurantServiceClient;
import pl.restaurant.orderservice.business.service.order.RestaurantOrderService;
import pl.restaurant.orderservice.data.entity.RestaurantOrderEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;
import pl.restaurant.orderservice.data.repository.RestaurantOrderMealRepo;
import pl.restaurant.orderservice.data.repository.RestaurantOrderRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantOrderServiceTest extends MysqlTest {
    @Autowired
    private RestaurantOrderService service;

    @Autowired
    private RestaurantOrderRepo orderRepo;

    @Autowired
    private RestaurantOrderMealRepo restaurantOrderMealRepo;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private RestaurantServiceClient restaurantService;

    @MockBean
    private LoginService loginService;

    @MockBean
    private MenuServiceClient menuService;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void clear() {
        orderRepo.deleteAll();
    }

    @Test
    public void addRestaurantOrderSuccess() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        //when
        when(restaurantService.isRestaurantTableExists(any(Long.class), any(Long.class))).thenReturn(true);
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn(null);
        service.addRestaurantOrder(order);
        List<RestaurantOrderEntity> all = orderRepo.getActiveOrders(1L);
        List<RestaurantOrderMealEntity> meals = restaurantOrderMealRepo.findAll();
        //then
        assertThat(all).hasSize(1);
        assertNull(all.get(0).getDeliveryDate());
        assertThat(all.get(0).getOrderDate().toLocalDate()).isEqualTo(LocalDate.now());
        assertNotNull(all.get(0).getOrderId());
        assertThat(meals).hasSize(1);
        assertThat(meals.get(0).getId().getOrderId()).isEqualTo(all.get(0).getOrderId());
    }

    @Test
    public void addRestaurantOrderFailureTableInRestaurantDoesNotExist() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        //when
        when(restaurantService.isRestaurantTableExists(any(Long.class), any(Long.class))).thenReturn(false);
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn(null);
        //then
        assertThrows(TableNotFoundException.class, () -> service.addRestaurantOrder(order));
    }

    @Test
    public void addRestaurantOrderFailureMenuValidationFailed() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        //when
        when(restaurantService.isRestaurantTableExists(any(Long.class), any(Long.class))).thenReturn(true);
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn("Validaton failed");
        //then
        assertThrows(InvalidOrderException.class, () -> service.addRestaurantOrder(order));
    }

    @Test
    public void updateRestaurantOrderSuccess() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        Long orderId = orderRepo.save(RestaurantOrderMapper.mapObjectToData(order)).getOrderId();
        BigDecimal newPrice = BigDecimal.valueOf(40);
        order.setPrice(newPrice);
        //when
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn(null);
        service.updateOrder(orderId, order);
        List<RestaurantOrderEntity> all = orderRepo.getActiveOrders(1L);
        List<RestaurantOrderMealEntity> meals = restaurantOrderMealRepo.findAll();
        //then
        assertThat(meals).hasSize(1);
        assertThat(meals.get(0).getId().getOrderId()).isEqualTo(all.get(0).getOrderId());
        assertThat(all.get(0).getPrice().compareTo(newPrice)).isEqualTo(0);
    }

    @Test
    public void updateRestaurantOrderFailureOrderNotFound() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        orderRepo.save(RestaurantOrderMapper.mapObjectToData(order));
        BigDecimal newPrice = BigDecimal.valueOf(40);
        order.setPrice(newPrice);
        //when
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn(null);
        //then
        assertThrows(OrderNotFoundException.class, () -> service.updateOrder(-1L, order));
    }

    @Test
    public void updateRestaurantOrderFailureCannotUpdateDeliveredOrder() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        RestaurantOrderEntity restaurantOrder = RestaurantOrderMapper.mapObjectToData(order);
        restaurantOrder.setDeliveryDate(LocalDateTime.now());
        Long orderId = orderRepo.save(restaurantOrder).getOrderId();
        BigDecimal newPrice = BigDecimal.valueOf(40);
        order.setPrice(newPrice);
        //when
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn(null);
        //then
        assertThrows(CannotUpdateOrderException.class, () -> service.updateOrder(orderId, order));
    }

    @Test
    public void updateRestaurantOrderFailureMenuValidationException() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        Long orderId = orderRepo.save(RestaurantOrderMapper.mapObjectToData(order)).getOrderId();
        BigDecimal newPrice = BigDecimal.valueOf(40);
        order.setPrice(newPrice);
        //when
        when(loginService.login()).thenReturn(new KeycloakResponse());
        when(menuService.validateOrders(any(Long.class), any(), any())).thenReturn("Validation failure");
        //then
        assertThrows(InvalidOrderException.class, () -> service.updateOrder(orderId, order));
    }

    @Test
    public void completeOrderSuccess() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        Long orderId = orderRepo.save(RestaurantOrderMapper.mapObjectToData(order)).getOrderId();
        //when
        service.completeOrder(orderId);
        List<RestaurantOrderEntity> all = orderRepo.findAll();
        //then
        assertNotNull(all.get(0).getDeliveryDate());
    }

    @Test
    public void completeOrderFailureOrderNotFound() {
        //given
        RestaurantOrder order = getRestaurantOrder();
        order.setPrice(BigDecimal.ZERO);
        Long orderId = orderRepo.save(RestaurantOrderMapper.mapObjectToData(order)).getOrderId();
        //when
        //then
        assertThrows(OrderNotFoundException.class, () -> service.completeOrder(-1L));
    }

    private RestaurantOrder getRestaurantOrder() {
        return new RestaurantOrder().builder()
                .restaurantId(1L)
                .tableId(1L)
                .orders(getOrders())
                .price(BigDecimal.valueOf(30))
                .build();
    }

    private List<Order> getOrders() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order().builder()
                .dishId(1)
                .name("Ogórkowa")
                .amount(1)
                .price(BigDecimal.valueOf(30))
                .build());
        return orderList;
    }
}
