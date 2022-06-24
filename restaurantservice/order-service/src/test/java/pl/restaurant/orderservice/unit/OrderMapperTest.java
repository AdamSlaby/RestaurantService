package pl.restaurant.orderservice.unit;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import pl.restaurant.orderservice.api.mapper.OrderMapper;
import pl.restaurant.orderservice.api.response.OrderInfo;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;
import pl.restaurant.orderservice.data.entity.RestaurantOrderMealEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderMapperTest {

    @Test
    public void mapDataToObjectSuccessOnlineOrder() {
        //given
        OnlineOrderMealEntity onlineOrderMealEntity = new EasyRandom(getParameters())
                .nextObject(OnlineOrderMealEntity.class);
        //when
        OrderInfo orderInfo = OrderMapper.mapDataToObject(onlineOrderMealEntity);
        //then
        assertNotNull(orderInfo);
        assertThat(orderInfo.getDishId()).isEqualTo(onlineOrderMealEntity.getId().getMealId());
        assertThat(orderInfo.getName()).isEqualTo(onlineOrderMealEntity.getMealName());
        assertThat(orderInfo.getAmount()).isEqualTo(onlineOrderMealEntity.getQuantity());
        assertThat(orderInfo.getPrice().compareTo(onlineOrderMealEntity.getPrice())).isEqualTo(0);
    }

    @Test
    public void mapDataToObjectSuccessRestaurantOrder() {
        //given
        RestaurantOrderMealEntity restaurantOrderMealEntity = new EasyRandom(getParameters())
                .nextObject(RestaurantOrderMealEntity.class);
        //when
        OrderInfo orderInfo = OrderMapper.mapDataToObject(restaurantOrderMealEntity);
        //then
        assertNotNull(orderInfo);
        assertThat(orderInfo.getDishId()).isEqualTo(restaurantOrderMealEntity.getId().getMealId());
        assertThat(orderInfo.getName()).isEqualTo(restaurantOrderMealEntity.getMealName());
        assertThat(orderInfo.getAmount()).isEqualTo(restaurantOrderMealEntity.getQuantity());
        assertThat(orderInfo.getPrice().compareTo(restaurantOrderMealEntity.getPrice())).isEqualTo(0);
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
