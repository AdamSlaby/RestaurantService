package pl.restaurant.supplyservice.business.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.restaurant.supplyservice.api.mapper.AddressMapper;
import pl.restaurant.supplyservice.api.mapper.InvoiceMapper;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.api.request.Invoice;
import pl.restaurant.supplyservice.api.request.InvoiceFilters;
import pl.restaurant.supplyservice.api.request.SortDirection;
import pl.restaurant.supplyservice.api.response.InvoiceListView;
import pl.restaurant.supplyservice.api.response.InvoiceShortInfo;
import pl.restaurant.supplyservice.api.response.InvoiceView;
import pl.restaurant.supplyservice.api.response.RestaurantShortInfo;
import pl.restaurant.supplyservice.business.exception.*;
import pl.restaurant.supplyservice.business.utility.Validator;
import pl.restaurant.supplyservice.data.entity.AddressEntity;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;
import pl.restaurant.supplyservice.data.repository.AddressRepo;
import pl.restaurant.supplyservice.data.repository.InvoiceRepo;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final static int AMOUNT = 10;
    private RestaurantServiceClient restaurantClient;
    private MenuServiceClient menuClientService;
    private InvoiceRepo invoiceRepo;
    private AddressRepo addressRepo;
    private GoodService goodService;


    @Override
    public InvoiceView getInvoice(String invoiceNr) {
        InvoiceEntity invoiceEntity = invoiceRepo.getByInvoiceNr(invoiceNr)
                .orElseThrow(InvoiceNotFoundException::new);
        RestaurantShortInfo restaurantShortInfo = restaurantClient
                .getRestaurantShortInfo(invoiceEntity.getRestaurantId());
        return InvoiceMapper.mapDataToView(invoiceEntity, restaurantShortInfo);
    }

    @Override
    public InvoiceListView getInvoiceList(InvoiceFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<InvoiceShortInfo> page = invoiceRepo.
                getInvoiceList(filters.getNr(), filters.getDate(), filters.getSellerName(),
                        filters.getRestaurantId(), pageable);
        return new InvoiceListView().builder()
                .totalElements(page.getTotalElements())
                .invoices(page.getContent())
                .build();
    }

    @Override
    @Transactional
    public void addInvoice(Invoice invoice) {
        validateInvoice(invoice);
        if (invoiceRepo.existsByInvoiceNr(invoice.getNr()))
            throw new InvoiceAlreadyExistsException();
        AddressEntity buyerAddress = addressRepo
                .save(AddressMapper.mapObjectToData(invoice.getBuyerAddress()));
        AddressEntity sellerAddress = addressRepo
                .save(AddressMapper.mapObjectToData(invoice.getSellerAddress()));
        InvoiceEntity invoiceEntity = invoiceRepo
                .save(InvoiceMapper.mapObjectToData(invoice, buyerAddress, sellerAddress));
       addGoodsForInvoice(invoiceEntity, invoice, true);
    }

    @Override
    public void updateInvoice(String invoiceNr, Invoice invoice) {
        validateInvoice(invoice);
        InvoiceEntity invoiceEntity = invoiceRepo.getByInvoiceNr(invoiceNr)
                .orElseThrow(InvoiceNotFoundException::new);
        if (invoiceRepo.existsByInvoiceNr(invoice.getNr()) && !invoiceEntity.getInvoiceNr().equals(invoice.getNr()))
            throw new InvoiceAlreadyExistsException();
        goodService.deleteAllByInvoice(invoiceEntity);
        updateInvoice(invoiceEntity, invoice);
    }

    @Transactional
    public void updateInvoice(InvoiceEntity invoiceEntity, Invoice invoice) {
        InvoiceMapper.updateInvoice(invoiceEntity, invoice);
        AddressMapper.updateAddress(invoiceEntity.getBuyerAddress(), invoice.getBuyerAddress());
        AddressMapper.updateAddress(invoiceEntity.getSellerAddress(), invoice.getSellerAddress());
        invoiceRepo.save(invoiceEntity);
        addressRepo.save(invoiceEntity.getBuyerAddress());
        addressRepo.save(invoiceEntity.getSellerAddress());
        addGoodsForInvoice(invoiceEntity, invoice, false);
    }

    private Pageable mapSortEventToPageable(InvoiceFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                InvoiceEntity.class.getDeclaredField(column);
                if (filters.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filters.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }

    private void validateInvoice(Invoice invoice) {
        Validator.validateNip(invoice.getBuyerNip(), "buyerNip");
        Validator.validateNip(invoice.getSellerNip(), "sellerNip");
        if (!restaurantClient.isRestaurantExist(invoice.getRestaurantId()))
            throw new RestaurantNotFoundException();
        if (!menuClientService.isIngredientsExists(invoice.getGoods().stream()
                .map(Good::getIngredientId).collect(Collectors.toList())))
            throw new IngredientNotFoundException();
    }

    private void addGoodsForInvoice(InvoiceEntity invoiceEntity, Invoice invoice, boolean updateSupply) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < invoice.getGoods().size(); i++) {
            builder.setLength(0);
            goodService.addGood(invoice.getGoods().get(i), invoiceEntity, builder
                    .append("goods[").append(i).append("].").toString(), updateSupply);
        }
    }
}
