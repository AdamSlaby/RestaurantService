package pl.restaurant.supplyservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.supplyservice.api.request.IngredientView;
import pl.restaurant.supplyservice.api.request.NewSupply;
import pl.restaurant.supplyservice.api.request.OrderValidation;
import pl.restaurant.supplyservice.api.request.SupplyInfo;
import pl.restaurant.supplyservice.api.response.Unit;
import pl.restaurant.supplyservice.business.exception.RestaurantNotFoundException;
import pl.restaurant.supplyservice.business.exception.SupplyAlreadyExistsException;
import pl.restaurant.supplyservice.business.exception.SupplyErrorException;
import pl.restaurant.supplyservice.business.service.MenuServiceClient;
import pl.restaurant.supplyservice.business.service.RestaurantServiceClient;
import pl.restaurant.supplyservice.business.service.SupplyService;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.SupplyRepo;
import pl.restaurant.supplyservice.data.repository.UnitRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class SupplyServiceTest {
    @Autowired
    private SupplyService supplyService;

    @Autowired
    private SupplyRepo supplyRepo;

    @Autowired
    private UnitRepo unitRepo;

    @MockBean
    private RestaurantServiceClient restaurantClient;

    @MockBean
    private MenuServiceClient menuClient;

    @BeforeEach
    public void fillDatabase() {
        unitRepo.save(getUnit("kg"));
        unitRepo.save(getUnit("dag"));
        unitRepo.save(getUnit("g"));
        supplyRepo.save(getSupply(1, 1));
        supplyRepo.save(getSupply(1, 2));
        supplyRepo.save(getSupply(1, 3));
    }

    @AfterEach
    public void clear() {
        supplyRepo.deleteAll();
        unitRepo.deleteAll();
    }

    @Test
    public void addSupplySuccess() {
        //given
        NewSupply supply = new NewSupply(1L, "Mięso", BigDecimal.valueOf(1),
                getUnit("kg").getUnitId());
        String authorization = "Bearer token";
        //when
        when(menuClient.getIngredient(any(String.class), any(String.class))).thenReturn(4);
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        SupplyInfo supplyInfo = supplyService.addSupply(supply, authorization);
        //then
        assertThat(supplyInfo.getIngredientId()).isEqualTo(4);
        assertThat(supplyInfo.getQuantity()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(supplyInfo.getUnitId()).isEqualTo(getUnit("kg").getUnitId());
        assertThat(supplyInfo.getRestaurantId()).isEqualTo(1L);
    }

    @Test
    public void addSupplyFailureRestaurantDoesNotExist() {
        //given
        NewSupply supply = new NewSupply(1L, "Mięso", BigDecimal.valueOf(1),
                getUnit("kg").getUnitId());
        String authorization = "Bearer token";
        //when
        when(menuClient.getIngredient(any(String.class), any(String.class))).thenReturn(4);
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(false);
        //then
        assertThrows(RestaurantNotFoundException.class, () -> supplyService.addSupply(supply, authorization));
    }

    @Test
    public void addSupplyFailureSupplyAlreadyExists() {
        //given
        NewSupply supply = new NewSupply(1L, "Mięso", BigDecimal.valueOf(1),
                getUnit("kg").getUnitId());
        String authorization = "Bearer token";
        //when
        when(menuClient.getIngredient(any(String.class), any(String.class))).thenReturn(3);
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        //then
        assertThrows(SupplyAlreadyExistsException.class, () -> supplyService.addSupply(supply, authorization));
    }

    @Test
    public void checkSuppliesSuccess() {
        //given
        Long restaurantId = 1L;
        OrderValidation order = getOrderValidation(BigDecimal.valueOf(50));
        //when
        //then
        assertDoesNotThrow(() -> supplyService.checkSupplies(restaurantId, order));
    }

    @Test
    public void checkSuppliesFailureNotEnoughSupplies() {
        //given
        Long restaurantId = 1L;
        OrderValidation order = getOrderValidation(BigDecimal.valueOf(50.1));
        //when
        //then
        assertThrows(SupplyErrorException.class, () -> supplyService.checkSupplies(restaurantId, order));
    }

    @Test
    public void updateSuppliesSuccess() {
        //given
        Long restaurantId = 1L;
        List<OrderValidation> orders = new ArrayList<>();
        orders.add(getOrderValidation(BigDecimal.valueOf(50)));
        //when
        supplyService.updateSupplies(restaurantId, orders);
        List<SupplyInfo> supplies = supplyRepo.getAllSupplies(1L);
        //then
        assertThat(supplies.get(0).getQuantity().compareTo(BigDecimal.ZERO)).isEqualTo(0);
        assertThat(supplies.get(1).getQuantity().compareTo(BigDecimal.ZERO)).isEqualTo(0);
        assertThat(supplies.get(2).getQuantity().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void updateSuppliesFailure() {
        //given
        Long restaurantId = 1L;
        List<OrderValidation> orders = new ArrayList<>();
        orders.add(getOrderValidation(BigDecimal.valueOf(50.1)));
        //when
        //then
        assertThrows(SupplyErrorException.class, () -> supplyService.updateSupplies(restaurantId, orders));
    }

    private SupplyEntity getSupply(long restaurantId, int ingredientId) {
        return new SupplyEntity().builder()
                .restaurantIngredientId(new RestaurantIngredientId(restaurantId, ingredientId))
                .quantity(BigDecimal.valueOf(1))
                .unit(getUnit("kg"))
                .build();
    }

    private UnitEntity getUnit(String name) {
        Optional<UnitEntity> unit = unitRepo.findByName(name);
        return unit.orElseGet(() -> unitRepo.save(new UnitEntity().builder()
                .name(name)
                .build()));
    }

    private OrderValidation getOrderValidation(BigDecimal quantity) {
        return new OrderValidation().builder()
                .dishId(1)
                .name("Pomidorowa")
                .amount(2)
                .ingredients(getIngredientList(quantity))
                .build();
    }

    private List<IngredientView> getIngredientList(BigDecimal quantity) {
        List<IngredientView> ingredients = new ArrayList<>();
        ingredients.add(new IngredientView(1, "", quantity,
                new Unit(getUnit("dag").getUnitId(), "dag")));
        ingredients.add(new IngredientView(2, "", quantity,
                new Unit(getUnit("dag").getUnitId(), "dag")));
        ingredients.add(new IngredientView(3, "", quantity,
                new Unit(getUnit("dag").getUnitId(), "dag")));
        return ingredients;
    }
}
