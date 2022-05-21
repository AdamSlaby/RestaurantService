package pl.restaurant.supplyservice.api.mapper;

import lombok.experimental.UtilityClass;
import pl.restaurant.supplyservice.api.request.Invoice;
import pl.restaurant.supplyservice.api.response.InvoiceView;
import pl.restaurant.supplyservice.api.response.RestaurantShortInfo;
import pl.restaurant.supplyservice.data.entity.AddressEntity;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;

import java.util.stream.Collectors;

@UtilityClass
public class InvoiceMapper {

    public static InvoiceView mapDataToView(InvoiceEntity invoice, RestaurantShortInfo info) {
        return new InvoiceView().builder()
                .nr(invoice.getInvoiceNr())
                .restaurantId(invoice.getRestaurantId())
                .restaurantInfo(info)
                .date(invoice.getDate())
                .sellerName(invoice.getSellerName())
                .buyerName(invoice.getBuyerName())
                .sellerAddress(AddressMapper.mapDataToObject(invoice.getSellerAddress()))
                .buyerAddress(AddressMapper.mapDataToObject(invoice.getBuyerAddress()))
                .sellerNip(invoice.getSellerNip())
                .buyerNip(invoice.getBuyerNip())
                .completionDate(invoice.getCompletionDate())
                .price(invoice.getPrice())
                .goods(invoice.getGoods().stream()
                        .map(GoodMapper::mapDataToView)
                        .collect(Collectors.toList()))
                .build();
    }

    public static InvoiceEntity mapObjectToData(Invoice invoice, AddressEntity buyerAddress,
                                                AddressEntity sellerAddress) {
        return new InvoiceEntity().builder()
                .invoiceNr(invoice.getNr())
                .restaurantId(invoice.getRestaurantId())
                .date(invoice.getDate())
                .sellerName(invoice.getSellerName())
                .buyerName(invoice.getBuyerName())
                .sellerNip(invoice.getSellerNip())
                .buyerNip(invoice.getBuyerNip())
                .completionDate(invoice.getCompletionDate())
                .price(invoice.getPrice())
                .sellerAddress(sellerAddress)
                .buyerAddress(buyerAddress)
                .build();
    }

    public static void updateInvoice(InvoiceEntity invoiceEntity, Invoice invoice) {
        invoiceEntity.setInvoiceNr(invoice.getNr());
        invoiceEntity.setRestaurantId(invoice.getRestaurantId());
        invoiceEntity.setDate(invoice.getDate());
        invoiceEntity.setSellerName(invoice.getSellerName());
        invoiceEntity.setBuyerName(invoice.getBuyerName());
        invoiceEntity.setSellerNip(invoice.getSellerNip());
        invoiceEntity.setBuyerNip(invoice.getBuyerNip());
        invoiceEntity.setCompletionDate(invoice.getCompletionDate());
        invoiceEntity.setPrice(invoice.getPrice());
    }
}
