package pl.restaurant.restaurantservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.restaurant.restaurantservice.data.entity.*;
import pl.restaurant.restaurantservice.data.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ReservationTableRepoTest {
    @Autowired
    ReservationTableRepo repo;

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    RestaurantRepo restaurantRepo;

    @Autowired
    TableRepo tableRepo;

    @Autowired
    RestaurantTableEntityRepo restaurantTableEntityRepo;

    private Long reservationId;
    private Long tableId;
    private Long restaurantId;

    @BeforeEach
    public void prepareDatabase() {
        EasyRandom generator = new EasyRandom(getParameters());
        RestaurantEntity restaurant = saveRestaurant(generator);
        this.restaurantId = restaurant.getRestaurantId();
        FoodTableEntity table = saveTable(generator);
        this.tableId = table.getTableId();
        RestaurantTableEntity restaurantTableEntity = saveRestaurantTable(generator, restaurant, table);
        ReservationEntity reservation = saveReservation(generator, 16, 18);
        ReservationTableEntity reservationTableEntity = saveReservationTable(generator, restaurantTableEntity, reservation);
        this.reservationId = reservationTableEntity.getReservation().getReservationId();
        ReservationEntity reservation2 = saveReservation(generator, 18, 20);
        saveReservationTable(generator, restaurantTableEntity, reservation2);
    }

    @Test
    public void deleteAllReservationsById() {
        //given
        //when
        repo.deleteAllByReservationId(this.reservationId);
        List<ReservationTableEntity> all = repo.findAll();
        //then
        assertThat(all).hasSize(1);
    }

    @Test
    public void deleteReservationsByNotExistingId() {
        //given
        //when
        repo.deleteAllByReservationId(-1L);
        List<ReservationTableEntity> all = repo.findAll();
        //then
        assertThat(all).hasSize(2);
    }

    @Test
    public void checkIfReservationExistsAtGivenHourAndWithGivenTableId() {
        //given
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(this.tableId);
        LocalDateTime fromHour = LocalDateTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0);
        //when
        Long reservations = repo.isReservationExists(tableIds, fromHour);
        //then
        assertThat(reservations).isEqualTo(1);
    }

    @Test
    public void checkIfReservationDoesNotExistAtGivenHour() {
        //given
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(this.tableId);
        LocalDateTime fromHour = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0);
        //when
        Long reservations = repo.isReservationExists(tableIds, fromHour);
        //then
        assertThat(reservations).isEqualTo(0);
    }

    @Test
    public void checkIfReservationDoesNotExistWithGivenTableId() {
        //given
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(-1L);
        LocalDateTime fromHour = LocalDateTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0);
        //when
        Long reservations = repo.isReservationExists(tableIds, fromHour);
        //then
        assertThat(reservations).isEqualTo(0);
    }

    @Test
    public void checkIfReservationDoesNotExistAtGivenHourWithGivenTableId() {
        //given
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(-1L);
        LocalDateTime fromHour = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0).withNano(0);
        //when
        Long reservations = repo.isReservationExists(tableIds, fromHour);
        //then
        assertThat(reservations).isEqualTo(0);
    }

    @Test
    public void getReservationFromGivenDateAndGivenRestaurantId() {
        //given
        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0);
        //when
        List<ReservationEntity> reservations = repo.getReservationsFromDate(from, to, restaurantId);
        //then
        assertThat(reservations).hasSize(2);
        assertThat(reservations.get(0).getFromHour().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(reservations.get(0).getToHour().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(reservations.get(1).getToHour().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(reservations.get(1).getToHour().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void getEmptyReservationListFromGivenDate() {
        //given
        LocalDateTime from = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime to = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59)
                .withSecond(59).withNano(0);
        //when
        List<ReservationEntity> reservations = repo.getReservationsFromDate(from, to, restaurantId);
        //then
        assertThat(reservations).hasSize(0);
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 3);
        return parameters;
    }

    private RestaurantEntity saveRestaurant(EasyRandom generator) {
        return restaurantRepo.save(generator.nextObject(RestaurantEntity.class));
    }

    private ReservationEntity saveReservation(EasyRandom generator, int from, int to) {
        ReservationEntity reservation = generator.nextObject(ReservationEntity.class);
        reservation.setTables(null);
        reservation.setFromHour(LocalDateTime.now().withHour(from).withMinute(0).withSecond(0).withNano(0));
        reservation.setToHour(LocalDateTime.now().withHour(to).withMinute(0).withSecond(0).withNano(0));
        return reservationRepo.save(reservation);
    }

    private FoodTableEntity saveTable(EasyRandom generator) {
        FoodTableEntity table = generator.nextObject(FoodTableEntity.class);
        return tableRepo.save(table);
    }

    private RestaurantTableEntity saveRestaurantTable(EasyRandom generator, RestaurantEntity restaurant, FoodTableEntity table) {
        RestaurantTableEntity restaurantTableEntity = generator.nextObject(RestaurantTableEntity.class);
        restaurantTableEntity.setRestaurantTableId(new RestaurantTableId(restaurant.getRestaurantId(), table.getTableId()));
        restaurantTableEntity.setRestaurant(restaurant);
        restaurantTableEntity.setTable(table);
        restaurantTableEntity.setReservationTables(null);
        return restaurantTableEntityRepo.save(restaurantTableEntity);
    }

    private ReservationTableEntity saveReservationTable(EasyRandom generator, RestaurantTableEntity restaurantTableEntity,
                                                        ReservationEntity reservation) {
        ReservationTableEntity reservationTableEntity = generator.nextObject(ReservationTableEntity.class);
        reservationTableEntity.getReservationTableId()
                .setReservationId(reservation.getReservationId());
        reservationTableEntity.getReservationTableId()
                .setTableId(restaurantTableEntity.getRestaurantTableId());
        reservationTableEntity.setReservation(reservation);
        reservationTableEntity.setRestaurantTable(restaurantTableEntity);
        return repo.save(reservationTableEntity);
    }
}
