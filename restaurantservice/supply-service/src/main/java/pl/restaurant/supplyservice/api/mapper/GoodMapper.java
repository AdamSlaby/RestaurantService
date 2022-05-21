package pl.restaurant.supplyservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.api.response.GoodView;
import pl.restaurant.supplyservice.data.entity.GoodEntity;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;

@UtilityClass
public class GoodMapper {
    public static GoodView mapDataToView(GoodEntity good) {
        return new GoodView().builder()
                .id(good.getGoodId())
                .ingredientId(good.getIngredientId())
                .quantity(good.getQuantity())
                .unitId(good.getUnit().getUnitId())
                .unitNetPrice(good.getUnitNetPrice())
                .discount(good.getDiscount())
                .netPrice(good.getNetPrice())
                .taxType(good.getTaxType())
                .taxPrice(good.getTaxPrice())
                .build();
    }

    public static GoodEntity mapObjectToData(Good good, InvoiceEntity invoice, UnitEntity unit) {
        return new GoodEntity().builder()
                .ingredientId(good.getIngredientId())
                .quantity(good.getQuantity())
                .unitNetPrice(good.getUnitNetPrice())
                .discount(good.getDiscount())
                .netPrice(good.getNetPrice())
                .taxType(good.getTaxType())
                .taxPrice(good.getTaxPrice())
                .unit(unit)
                .invoice(invoice)
                .build();
    }
}
