package pl.restaurant.restaurantservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import pl.restaurant.restaurantservice.api.response.AvailableHour;
import pl.restaurant.restaurantservice.business.templetemethod.AvailableHoursForSmallGroupTemplate;
import pl.restaurant.restaurantservice.business.templetemethod.AvailableHoursTemplate;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.ReservationEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

public class AvailableHoursTemplateTest {

    @Test
    public void getAvailableHoursForSmallGroupSuccess() {
        //given
        AvailableHoursTemplate template = new AvailableHoursForSmallGroupTemplate();
        Set<FoodTableEntity> tables = new LinkedHashSet<>(getTables());
        LocalDateTime date = LocalDateTime.now();
        List<ReservationEntity> reservations = getReservations();
        //when
        List<AvailableHour> availableHours = template.getAvailableHours(tables, date, reservations, 4);
        //then
        assertThat(availableHours).hasSize(5);
        assertThat(availableHours.get(0).getHour().getHour()).isEqualTo(10);
        assertThat(availableHours.get(1).getHour().getHour()).isEqualTo(12);
        assertThat(availableHours.get(2).getHour().getHour()).isEqualTo(14);
        assertThat(availableHours.get(3).getHour().getHour()).isEqualTo(16);
        assertThat(availableHours.get(4).getHour().getHour()).isEqualTo(20);
        assertThat(availableHours.get(0).getTables().get(0)).isEqualTo(3);
    }

    @Test
    public void getAvailableHoursForZeroClients() {
        //given
        AvailableHoursTemplate template = new AvailableHoursForSmallGroupTemplate();
        Set<FoodTableEntity> tables = new LinkedHashSet<>(getTables());
        LocalDateTime date = LocalDateTime.now();
        List<ReservationEntity> reservations = getReservations();
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> template.getAvailableHours(tables, date, reservations, 0));
    }

    @Test
    public void getAvailableHoursForLargeGroupSuccess() {
        //given
        AvailableHoursTemplate template = new AvailableHoursForSmallGroupTemplate();
        Set<FoodTableEntity> tables = new LinkedHashSet<>(getTables());
        LocalDateTime date = LocalDateTime.now();
        List<ReservationEntity> reservations = getReservations();
        //when
        List<AvailableHour> availableHours = template.getAvailableHours(tables, date, reservations, 14);
        //then
        assertThat(availableHours).hasSize(5);
        assertThat(availableHours.get(0).getHour().getHour()).isEqualTo(10);
        assertThat(availableHours.get(1).getHour().getHour()).isEqualTo(12);
        assertThat(availableHours.get(2).getHour().getHour()).isEqualTo(14);
        assertThat(availableHours.get(3).getHour().getHour()).isEqualTo(16);
        assertThat(availableHours.get(4).getHour().getHour()).isEqualTo(20);
        assertThat(availableHours.get(0).getTables()).hasSize(4);
    }

    @Test
    public void getAvailableHoursForTooLargeGroup() {
        //given
        AvailableHoursTemplate template = new AvailableHoursForSmallGroupTemplate();
        Set<FoodTableEntity> tables = new LinkedHashSet<>(getTables());
        LocalDateTime date = LocalDateTime.now();
        List<ReservationEntity> reservations = getReservations();
        //when
        List<AvailableHour> availableHours = template.getAvailableHours(tables, date, reservations, 15);
        //then
        assertThat(availableHours).hasSize(0);
    }

    private List<FoodTableEntity> getTables() {
        List<FoodTableEntity> tables = new ArrayList<>();
        tables.add(new FoodTableEntity().builder().tableId(1L).seatsNr(2).build());
        tables.add(new FoodTableEntity().builder().tableId(2L).seatsNr(3).build());
        tables.add(new FoodTableEntity().builder().tableId(3L).seatsNr(4).build());
        tables.add(new FoodTableEntity().builder().tableId(4L).seatsNr(5).build());
        return tables;
    }

    private List<ReservationEntity> getReservations() {
        List<ReservationEntity> reservations = new ArrayList<>();
        EasyRandom generator = new EasyRandom(getParameters());
        ReservationEntity reservationEntity = generator.nextObject(ReservationEntity.class);
        reservationEntity.setFromHour(LocalDateTime.now().withHour(18).withMinute(0).withSecond(0).withNano(0));
        reservationEntity.setToHour(LocalDateTime.now().withHour(20).withMinute(0).withSecond(0).withNano(0));
        FoodTableEntity table = new ArrayList<>(reservationEntity.getTables()).get(0).getRestaurantTable().getTable();
        table.setTableId(3L);
        table.setSeatsNr(4);
        FoodTableEntity table2 = new ArrayList<>(reservationEntity.getTables()).get(1).getRestaurantTable().getTable();
        table2.setTableId(4L);
        table2.setSeatsNr(5);
        FoodTableEntity table3 = new ArrayList<>(reservationEntity.getTables()).get(2).getRestaurantTable().getTable();
        table3.setTableId(2L);
        table3.setSeatsNr(3);
        reservations.add(reservationEntity);
        return reservations;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(4, 4);
        return parameters;
    }
}
