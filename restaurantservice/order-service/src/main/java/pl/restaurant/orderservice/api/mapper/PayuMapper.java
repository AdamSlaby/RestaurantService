package pl.restaurant.orderservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.orderservice.OrderServiceApplication;
import pl.restaurant.orderservice.api.response.payu.Payload;
import pl.restaurant.orderservice.data.entity.OnlineOrderEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@UtilityClass
public class PayuMapper {
    private static final String CURRENCY_CODE = "PLN";
    private static final String CUSTOMER_IP = "127.0.0.1";

    public static Payload mapOrderToPayload(OnlineOrderEntity order, String posId,
                                            String notifyUrl, String orderDescription) {
        return new Payload().builder()
                .notifyUrl(notifyUrl)
                .customerIp(CUSTOMER_IP)
                .continueUrl(OrderServiceApplication.FRONT_SITE)
                .merchantPosId(posId)
                .description(orderDescription)
                .currencyCode(CURRENCY_CODE)
                .totalAmount(order.getPrice().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.UNNECESSARY).toString())
                .products(order.getMeals().stream()
                        .map(ProductMapper::mapDataToProduct)
                        .collect(Collectors.toList()))
                .build();
    }
}
