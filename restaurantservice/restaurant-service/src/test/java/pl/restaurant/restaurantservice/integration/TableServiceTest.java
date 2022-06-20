package pl.restaurant.restaurantservice.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.restaurant.restaurantservice.business.service.TableService;
import pl.restaurant.restaurantservice.business.service.TableServiceImpl;
import pl.restaurant.restaurantservice.data.entity.FoodTableEntity;
import pl.restaurant.restaurantservice.data.repository.TableRepo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
public class TableServiceTest {
    @Autowired
    TableServiceImpl tableService;

    @Autowired
    TableRepo repo;

    @Test
    public void createTableSuccess() {
        //given
        int seatsNr = 5;
        //when
        tableService.createTable(seatsNr);
        List<FoodTableEntity> all = repo.findAll();
        //then
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getSeatsNr()).isEqualTo(5);
    }

    @Test
    public void createTableFailure() {
        //given
        int seatsNr = -1;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.createTable(seatsNr));
    }
}
