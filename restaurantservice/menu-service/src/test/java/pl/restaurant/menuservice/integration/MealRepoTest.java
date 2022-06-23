package pl.restaurant.menuservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.restaurant.menuservice.api.response.MealShortInfo;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.TypeEntity;
import pl.restaurant.menuservice.data.repository.MealRepo;
import pl.restaurant.menuservice.data.repository.TypeRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class MealRepoTest {
    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private TypeRepo typeRepo;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void fillDatabase() {
        mealRepo.save(getMeal("Pomidorowa", "Zupy"));
        mealRepo.save(getMeal("Ogórkowa", "Zupy"));
        mealRepo.save(getMeal("Filet z kurczaka", "Dania główne"));
    }

    @Test
    public void getMealsByMealNameSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String mealName = "Pomidorowa";
        //when
        Page<MealShortInfo> page = mealRepo.getMeals(mealName, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo(mealName);
    }

    @Test
    public void getMealsByTypeIdSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String typeName = "Zupy";
        int typeId = getTypeEntity(typeName).getTypeId();
        //when
        Page<MealShortInfo> page = mealRepo.getMeals(null, typeId, pageable);
        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getType()).isEqualTo(typeName);
        assertThat(page.getContent().get(1).getType()).isEqualTo(typeName);
    }

    @Test
    public void getMealsByAllParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        String typeName = "Zupy";
        int typeId = getTypeEntity(typeName).getTypeId();
        String mealName = "Pomidorowa";
        //when
        Page<MealShortInfo> page = mealRepo.getMeals(mealName, typeId, pageable);
        //then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo(mealName);
        assertThat(page.getContent().get(0).getType()).isEqualTo(typeName);
    }

    @Test
    public void getMealsByNullParametersSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<MealShortInfo> page = mealRepo.getMeals(null, null, pageable);
        //then
        assertThat(page.getContent()).hasSize(3);
    }

    @Test
    public void updateMealBestStatusInSuccess() {
        //given
        boolean status = true;
        Integer soupId = mealRepo.findByName("Pomidorowa").get().getMealId();
        Integer soupId2 = mealRepo.findByName("Ogórkowa").get().getMealId();
        List<Integer> mealIds = Arrays.asList(soupId, soupId2);
        //when
        mealRepo.updateMealBestStatusIn(mealIds, status);
        em.clear();
        List<MealEntity> all = mealRepo.findAllByIsBest(true, PageRequest.of(0, 10));
        //then
        assertThat(all).hasSize(2);
        assertTrue(all.get(0).isBest());
        assertTrue(all.get(1).isBest());
    }

    @Test
    public void updateMealBestStatusInEmptyListSuccess() {
        //given
        boolean status = true;
        List<Integer> mealIds = new ArrayList<>();
        //when
        mealRepo.updateMealBestStatusIn(mealIds, status);
        em.clear();
        List<MealEntity> all = mealRepo.findAllByIsBest(true, PageRequest.of(0, 10));
        //then
        assertThat(all).hasSize(0);
    }

    private MealEntity getMeal(String mealName, String typeName) {
        EasyRandom generator = new EasyRandom(getParameters());
        MealEntity meal = generator.nextObject(MealEntity.class);
        meal.setName(mealName);
        meal.setBest(false);
        meal.setType(getTypeEntity(typeName));
        meal.setPrice(BigDecimal.valueOf(20.5));
        meal.setMenus(null);
        meal.setIngredients(null);
        return meal;
    }

    private TypeEntity getTypeEntity(String typeName) {
        Optional<TypeEntity> optType = typeRepo.findByName(typeName);
        return optType.orElseGet(() -> typeRepo.save(new TypeEntity().builder()
                .name(typeName)
                .build()));
    }

    private EasyRandomParameters getParameters() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(1, 3);
        return parameters;
    }
}
