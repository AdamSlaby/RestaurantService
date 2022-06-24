package pl.restaurant.orderservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.restaurant.orderservice.api.response.OrderShortInfo;
import pl.restaurant.orderservice.data.entity.AddressEntity;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;
import pl.restaurant.orderservice.data.repository.OnlineOrderRepo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OnlineOrderRepoTest extends MysqlTest {
    @Autowired
    private OnlineOrderRepo orderRepo;

    @MockBean
    private RestTemplate restTemplate;

    private long orderId;

    @BeforeEach
    public void fillDatabase() {
        this.orderId = orderRepo.save(getOnlineOrder(1L, LocalDateTime.now(), true)).getOrderId();
        orderRepo.save(getOnlineOrder(2L, LocalDateTime.now(), false));
        orderRepo.save(getOnlineOrder(1L, LocalDateTime.now().plusHours(3), false));
        orderRepo.save(getOnlineOrder(2L, LocalDateTime.now().minusHours(3), false));
    }

    @Test
    public void getDeliveredOrdersByRestaurantIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long restaurantId = 1L;
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(restaurantId, null, null, null, null, pageable);
        Long orderRestaurantId = orderRepo.getByOrderId(page.getContent().get(0).getId()).get().getRestaurantId();
        Long order2RestaurantId = orderRepo.getByOrderId(page.getContent().get(1).getId()).get().getRestaurantId();
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(orderRestaurantId).isEqualTo(restaurantId);
        assertThat(order2RestaurantId).isEqualTo(restaurantId);
    }

    @Test
    public void getDeliveredOrdersByOrderIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(null, this.orderId, null, null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(this.orderId);
    }

    @Test
    public void getDeliveredOrdersByDateSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(null, null, from, to, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(from)).isEqualTo(1);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(to)).isEqualTo(-1);
    }

    @Test
    public void getDeliveredOrdersByCompletionStatusSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        boolean isCompleted = true;
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(null, null, null, null, isCompleted, pageable);
        LocalDateTime deliveryDate = orderRepo.findById(page.getContent().get(0).getId()).get().getDeliveryDate();
        //then
        assertThat(page.getContent()).hasSize(1);
        assertNotNull(deliveryDate);
    }

    @Test
    public void getDeliveredOrdersBySeveralParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long restaurantId = 1L;

        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(restaurantId, null, from, to, null, pageable);
        Long orderRestaurantId = orderRepo.findById(page.getContent().get(0).getId()).get().getRestaurantId();
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(from)).isEqualTo(1);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(to)).isEqualTo(-1);
        assertThat(orderRestaurantId).isEqualTo(restaurantId);
    }

    @Test
    public void getDeliveredOrdersByAllParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        Long restaurantId = 1L;
        boolean isCompleted = true;
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(restaurantId, this.orderId, from, to, isCompleted, pageable);
        OnlineOrderEntity order = orderRepo.findById(page.getContent().get(0).getId()).get();
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(this.orderId);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(from)).isEqualTo(1);
        assertThat(page.getContent().get(0).getOrderDate().compareTo(to)).isEqualTo(-1);
        assertThat(order.getRestaurantId()).isEqualTo(restaurantId);
        assertNotNull(order.getDeliveryDate());
    }

    @Test
    public void getDeliveredOrdersByAllNullParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<OrderShortInfo> page = orderRepo
                .getDeliveredOrders(null, null, null, null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(4);
    }

    private OnlineOrderEntity getOnlineOrder(Long restaurantId, LocalDateTime from, Boolean isCompleted) {
        EasyRandom generator = new EasyRandom(getParameters());
        OnlineOrderEntity order = generator.nextObject(OnlineOrderEntity.class);
        order.setRestaurantId(restaurantId);
        order.setOrderDate(from);
        order.setAddress(getAddress());
        order.setMeals(null);
        if (!isCompleted)
            order.setDeliveryDate(null);
        return order;
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
