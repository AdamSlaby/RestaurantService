package pl.restaurant.supplyservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.restaurant.supplyservice.api.response.InvoiceShortInfo;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;
import pl.restaurant.supplyservice.data.repository.InvoiceRepo;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class InvoiceRepoTest {
    @Autowired
    private InvoiceRepo invoiceRepo;

    @BeforeEach
    public void fillDatabase() {
        invoiceRepo.save(getInvoice("210001", LocalDate.now(), "Mariuszex", 1L));
        invoiceRepo.save(getInvoice("210002", LocalDate.now().plusDays(1), "Mariuszex", 2L));
        invoiceRepo.save(getInvoice("210003", LocalDate.now(), "Mariuszex", 2L));
        invoiceRepo.save(getInvoice("210004", LocalDate.now().minusDays(1), "Wrocławix", 3L));
    }

    @Test
    public void getInvoiceListByNumberSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String nr = "210001";
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList(nr, null, "", null, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getNr()).isEqualTo(nr);
    }

    @Test
    public void getInvoiceListByDateSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate date = LocalDate.now();
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList("", date, "", null, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getDate()).isEqualTo(date);
        assertThat(page.getContent().get(1).getDate()).isEqualTo(date);
    }

    @Test
    public void getInvoiceListBySellerNameSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String sellerName = "Wrocławix";
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList("", null, sellerName, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getSellerName()).isEqualTo(sellerName);
    }

    @Test
    public void getInvoiceListByRestaurantIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long restaurantId = 1L;
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList("", null, "", restaurantId, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getNr()).isEqualTo("210001");
    }

    @Test
    public void getInvoiceListByTwoParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Long restaurantId = 2L;
        String sellerName = "Mariuszex";
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList("", null, sellerName, restaurantId, pageable);
        Long invoiceRestaurantId = invoiceRepo.getByInvoiceNr(page.getContent().get(0).getNr()).get().getRestaurantId();
        Long invoice2RestaurantId = invoiceRepo.getByInvoiceNr(page.getContent().get(1).getNr()).get().getRestaurantId();
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getSellerName()).isEqualTo(sellerName);
        assertThat(page.getContent().get(1).getSellerName()).isEqualTo(sellerName);
        assertThat(invoiceRestaurantId).isEqualTo(restaurantId);
        assertThat(invoice2RestaurantId).isEqualTo(restaurantId);
    }

    @Test
    public void getInvoiceListByAllParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String nr = "210001";
        LocalDate date = LocalDate.now();
        String sellerName = "Mariuszex";
        Long restaurantId = 1L;
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList(nr, date, sellerName, restaurantId, pageable);
        Long invoiceRestaurantId = invoiceRepo.getByInvoiceNr(page.getContent().get(0).getNr()).get().getRestaurantId();
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getNr()).isEqualTo(nr);
        assertThat(page.getContent().get(0).getDate()).isEqualTo(date);
        assertThat(page.getContent().get(0).getSellerName()).isEqualTo(sellerName);
        assertThat(invoiceRestaurantId).isEqualTo(restaurantId);
    }

    @Test
    public void getEmptyInvoiceListByAllParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String nr = "210002";
        LocalDate date = LocalDate.now();
        String sellerName = "Mariuszex";
        Long restaurantId = 1L;
        //when
        Page<InvoiceShortInfo> page = invoiceRepo.getInvoiceList(nr, date, sellerName, restaurantId, pageable);
        //then
        assertThat(page.getContent()).hasSize(0);
    }

    private InvoiceEntity getInvoice(String nr, LocalDate date, String sellerName, Long restaurantId) {
        EasyRandom generator = new EasyRandom(getParameters());
        InvoiceEntity invoice = generator.nextObject(InvoiceEntity.class);
        invoice.setInvoiceNr(nr);
        invoice.setDate(date);
        invoice.setSellerName(sellerName);
        invoice.setRestaurantId(restaurantId);
        invoice.setGoods(null);
        invoice.setInvoiceId(null);
        invoice.getBuyerAddress().setAddressId(null);
        invoice.getSellerAddress().setAddressId(null);
        invoice.getSellerAddress().setBuyerInvoice(null);
        invoice.getSellerAddress().setSellerInvoice(null);
        invoice.getBuyerAddress().setBuyerInvoice(null);
        invoice.getBuyerAddress().setSellerInvoice(null);
        return invoice;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
