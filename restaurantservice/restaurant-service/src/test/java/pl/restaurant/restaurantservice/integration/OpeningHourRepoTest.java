package pl.restaurant.restaurantservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.restaurant.restaurantservice.data.entity.OpeningHourEntity;
import pl.restaurant.restaurantservice.data.entity.RestaurantEntity;
import pl.restaurant.restaurantservice.data.repository.OpeningHourRepo;
import pl.restaurant.restaurantservice.data.repository.RestaurantRepo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class OpeningHourRepoTest {
    @Autowired
    OpeningHourRepo repo;

    @Autowired
    RestaurantRepo restaurantRepo;

    @Test
    public void getOpeningHoursByRestaurantIdInAscOrder() {
        //given
        EasyRandom generator = new EasyRandom(getParameters());
        RestaurantEntity restaurantEntity = generator.nextObject(RestaurantEntity.class);
        RestaurantEntity restaurant = restaurantRepo.save(restaurantEntity);
        RestaurantEntity restaurant2 = restaurantRepo.save(restaurantEntity);
        repo.saveAll(getOpeningHours(restaurant));
        repo.saveAll(getOpeningHours(restaurant2));
        //when
        List<OpeningHourEntity> hours = repo.getHoursByRestaurant(restaurant.getRestaurantId());
        //then
        assertThat(hours).hasSize(7);
        assertThat(hours.get(0).getWeekDayNr()).isEqualTo(1);
        assertThat(hours.get(6).getWeekDayNr()).isEqualTo(7);
        assertThat(hours.get(0).getFromHour()).isEqualTo(LocalTime.now().withHour(8).withMinute(0)
                .withSecond(0).withNano(0));
        assertThat(hours.get(0).getToHour()).isEqualTo(LocalTime.now().withHour(16).withMinute(0)
                .withSecond(0).withNano(0));
    }

    @Test
    public void getOpeningHoursByRestaurantIdWhenDatabaseIsEmpty() {
        //given
        //when
        List<OpeningHourEntity> hours = repo.getHoursByRestaurant(1L);
        //then
        assertThat(hours).hasSize(0);
    }

    private List<OpeningHourEntity> getOpeningHours(RestaurantEntity restaurant) {
        List<OpeningHourEntity> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            list.add(new OpeningHourEntity().builder()
                    .weekDayNr(i)
                    .fromHour(LocalTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0))
                    .toHour(LocalTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0))
                    .restaurant(restaurant)
                    .build());
        }
        return list;
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 3);
        return parameters;
    }
}
