package pl.restaurant.supplyservice.component;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.supplyservice.api.mapper.AddressMapper;
import pl.restaurant.supplyservice.api.mapper.InvoiceMapper;
import pl.restaurant.supplyservice.api.request.Address;
import pl.restaurant.supplyservice.api.request.Good;
import pl.restaurant.supplyservice.api.request.Invoice;
import pl.restaurant.supplyservice.api.response.InvoiceView;
import pl.restaurant.supplyservice.business.service.MenuServiceClient;
import pl.restaurant.supplyservice.business.service.RestaurantServiceClient;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;
import pl.restaurant.supplyservice.data.entity.RestaurantIngredientId;
import pl.restaurant.supplyservice.data.entity.SupplyEntity;
import pl.restaurant.supplyservice.data.entity.UnitEntity;
import pl.restaurant.supplyservice.data.repository.InvoiceRepo;
import pl.restaurant.supplyservice.data.repository.SupplyRepo;
import pl.restaurant.supplyservice.data.repository.UnitRepo;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class InvoiceControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private SupplyRepo supplyRepo;

    @MockBean
    private RestaurantServiceClient restaurantClient;

    @MockBean
    private MenuServiceClient menuClient;

    private URL url;

    @BeforeEach
    public void setUp() throws Exception {
        unitRepo.save(getUnit("kg"));
        unitRepo.save(getUnit("dag"));
        unitRepo.save(getUnit("g"));
        supplyRepo.save(getSupply(1, 1));
        supplyRepo.save(getSupply(1, 2));
        supplyRepo.save(getSupply(1, 3));
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        supplyRepo.deleteAll();
        unitRepo.deleteAll();
        invoiceRepo.deleteAll();
    }

    @Test
    public void addInvoiceSuccess() {
        //given
        String invoiceNr = "210001";
        Invoice invoice = getInvoice();
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        when(menuClient.isIngredientsExists(any())).thenReturn(true);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "invoice/"), entity, String.class);
        InvoiceEntity savedInvoice = invoiceRepo.findAll().get(0);
        SupplyEntity supply = supplyRepo.findById(new RestaurantIngredientId(1L, 1)).get();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(savedInvoice);
        assertThat(savedInvoice.getInvoiceNr()).isEqualTo(invoiceNr);
        assertThat(supply.getQuantity().compareTo(BigDecimal.valueOf(1.5))).isEqualTo(0);
    }

    @Test
    public void addInvoiceFailureRestaurantDoesNotExist() {
        //given
        Invoice invoice = getInvoice();
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(false);
        when(menuClient.isIngredientsExists(any())).thenReturn(true);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "invoice/"), entity, String.class);
        List<InvoiceEntity> all = invoiceRepo.findAll();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void addInvoiceFailureIngredientDoesNotExist() {
        //given
        Invoice invoice = getInvoice();
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        when(menuClient.isIngredientsExists(any())).thenReturn(false);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "invoice/"), entity, String.class);
        List<InvoiceEntity> all = invoiceRepo.findAll();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void addInvoiceFailureSupplyDoesNotExist() {
        //given
        Invoice invoice = getInvoice();
        invoice.getGoods().get(0).setIngredientId(0);
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        when(menuClient.isIngredientsExists(any())).thenReturn(true);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "invoice/"), entity, String.class);
        List<InvoiceEntity> all = invoiceRepo.findAll();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void getInvoiceSuccess() {
        //given
        String invoiceNr = "210001";
        Invoice invoice = getInvoice();
        invoiceRepo.save(InvoiceMapper.mapObjectToData(invoice, AddressMapper
                .mapObjectToData(invoice.getBuyerAddress()), AddressMapper.mapObjectToData(invoice.getSellerAddress())));
        //when
        ResponseEntity<InvoiceView> response = restTemplate
                .getForEntity(this.url + "invoice/?nr=" + invoiceNr, InvoiceView.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNr()).isEqualTo(invoiceNr);
    }

    @Test
    public void getInvoiceFailureNrNotFound() {
        //given
        String invoiceNr = "210002";
        Invoice invoice = getInvoice();
        invoiceRepo.save(InvoiceMapper.mapObjectToData(invoice, AddressMapper
                .mapObjectToData(invoice.getBuyerAddress()), AddressMapper.mapObjectToData(invoice.getSellerAddress())));
        //when
        ResponseEntity<InvoiceView> response = restTemplate
                .getForEntity(this.url + "invoice/?nr=" + invoiceNr, InvoiceView.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateInvoiceSuccess() {
        //given
        String invoiceNr = "210001";
        String updatedInvoiceNr = "7/03-2018";
        Invoice invoice = getInvoice();
        invoiceRepo.save(InvoiceMapper.mapObjectToData(invoice, AddressMapper
                .mapObjectToData(invoice.getBuyerAddress()), AddressMapper.mapObjectToData(invoice.getSellerAddress())));
        invoice.setNr(updatedInvoiceNr);
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        when(menuClient.isIngredientsExists(any())).thenReturn(true);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "invoice/?nr=" + invoiceNr, HttpMethod.PUT, entity, String.class);
        Optional<InvoiceEntity> optInvoice = invoiceRepo.getByInvoiceNr(updatedInvoiceNr);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(optInvoice.isPresent());
        assertNotNull(optInvoice.get().getInvoiceId());
    }

    @Test
    public void updateInvoiceFailureInvoiceDoesNotExist() {
        //given
        String invoiceNr = "210002";
        String updatedInvoiceNr = "7/03-2018";
        Invoice invoice = getInvoice();
        invoiceRepo.save(InvoiceMapper.mapObjectToData(invoice, AddressMapper
                .mapObjectToData(invoice.getBuyerAddress()), AddressMapper.mapObjectToData(invoice.getSellerAddress())));
        invoice.setNr(updatedInvoiceNr);
        //when
        when(restaurantClient.isRestaurantExist(any(Long.class))).thenReturn(true);
        when(menuClient.isIngredientsExists(any())).thenReturn(true);
        HttpEntity<Invoice> entity = new HttpEntity<>(invoice);
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "invoice/?nr=" + invoiceNr, HttpMethod.PUT, entity, String.class);
        Optional<InvoiceEntity> optInvoice = invoiceRepo.getByInvoiceNr(invoiceNr);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertFalse(optInvoice.isPresent());
    }

    private Invoice getInvoice() {
        EasyRandom generator = new EasyRandom(getParameters());
        Invoice invoice = generator.nextObject(Invoice.class);
        invoice.setNr("210001");
        invoice.setRestaurantId(1L);
        invoice.setGoods(getGoods());
        invoice.setBuyerNip("5357883081");
        invoice.setSellerNip("5357883081");
        invoice.setPrice(BigDecimal.valueOf(10));
        invoice.setSellerAddress(getAddress());
        invoice.setBuyerAddress(getAddress());
        return invoice;
    }

    private Address getAddress() {
        return new Address().builder()
                .city("Kielce")
                .street("Warszawska")
                .flatNr("102")
                .houseNr("10")
                .postcode("25-300")
                .build();
    }

    private List<Good> getGoods() {
        EasyRandom generator = new EasyRandom(getParameters());
        List<Good> goods = new ArrayList<>();
        Good good = generator.nextObject(Good.class);
        good.setIngredientId(1);
        good.setQuantity(BigDecimal.valueOf(50));
        good.setUnitId(getUnit("dag").getUnitId());
        good.setUnitNetPrice(BigDecimal.valueOf(2));
        good.setDiscount(BigDecimal.valueOf(0));
        good.setNetPrice(BigDecimal.valueOf(1));
        good.setTaxPrice(BigDecimal.valueOf(0.1));
        goods.add(good);
        return goods;
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

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 1);
        return parameters;
    }
}
