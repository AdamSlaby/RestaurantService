package pl.restaurant.restaurantservice.unit;


import org.junit.jupiter.api.Test;
import pl.restaurant.restaurantservice.api.mapper.TableMapper;
import pl.restaurant.restaurantservice.api.response.Table;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantTableId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TableMapperTest {

    @Test
    public void entityIsMappedToPojo() {
        //given
        Set<RestaurantTableEntity> tables = new HashSet<>();
        tables.add(new RestaurantTableEntity().builder()
                .restaurantTableId(new RestaurantTableId(1L, 1L))
                .restaurant(new RestaurantEntity())
                .table(new FoodTableEntity().builder()
                        .tableId(1L)
                        .seatsNr(5)
                        .build())
                .reservationTables(null)
                .build());
        tables.add(new RestaurantTableEntity().builder()
                .restaurantTableId(new RestaurantTableId(1L, 2L))
                .restaurant(new RestaurantEntity())
                .table(new FoodTableEntity().builder()
                        .tableId(2L)
                        .seatsNr(4)
                        .build())
                .reservationTables(null)
                .build());
        //when
        List<Table> mappedTables = TableMapper.mapDataToObject(tables);
        //then
        assertThat(mappedTables).isNotNull();
        assertThat(mappedTables).hasSize(2);
        assertThat(mappedTables).contains(new Table(1L, 5));
        assertThat(mappedTables).contains(new Table(2L, 4));
    }
}
