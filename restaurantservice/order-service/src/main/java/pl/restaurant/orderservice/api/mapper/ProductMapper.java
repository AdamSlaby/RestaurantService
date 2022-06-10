package pl.restaurant.orderservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.orderservice.api.response.payu.Product;
import pl.restaurant.orderservice.data.entity.OnlineOrderMealEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class ProductMapper {

    public static Product mapDataToProduct(OnlineOrderMealEntity order) {
        return new Product().builder()
                .name(order.getMealName())
                .unitPrice(order.getPrice().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.UNNECESSARY).toString())
                .quantity(order.getQuantity().toString())
                .build();
    }
}
