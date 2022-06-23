package pl.restaurant.menuservice.integration;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.restaurant.menuservice.api.request.MealMenu;
import pl.restaurant.menuservice.api.response.Dish;
import pl.restaurant.menuservice.api.response.MealShortView;
import pl.restaurant.menuservice.api.response.MenuView;
import pl.restaurant.menuservice.business.exception.meal.MealAlreadyAddedToMenuException;
import pl.restaurant.menuservice.business.exception.meal.MealNotFoundException;
import pl.restaurant.menuservice.business.exception.menu.MenuNotFoundException;
import pl.restaurant.menuservice.business.service.MenuService;
import pl.restaurant.menuservice.business.service.MenuServiceImpl;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.MenuEntity;
import pl.restaurant.menuservice.data.entity.TypeEntity;
import pl.restaurant.menuservice.data.repository.MealRepo;
import pl.restaurant.menuservice.data.repository.MenuRepo;
import pl.restaurant.menuservice.data.repository.TypeRepo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private TypeRepo typeRepo;

    private Integer mealId;

    @BeforeEach
    public void fillDatabase() {
        MealEntity meal = getMeal("Pomidorowa", "Zupy");
        MealEntity meal2 = getMeal("Ogórkowa", "Zupy");
        MealEntity meal3 = getMeal("Barszcz biały", "Zupy");
        MealEntity meal4 = getMeal("Rosół", "Zupy");
        this.mealId = mealRepo.save(getMeal("Filet z kurczaka", "Dania główne")).getMealId();
        menuRepo.save(getMenu("Zima", List.of(meal)));
        menuRepo.save(getMenu("Wiosna", List.of(meal2)));
        menuRepo.save(getMenu("Lato", List.of(meal3)));
        menuRepo.save(getMenu("Jesień", List.of(meal4)));
    }

    @AfterEach
    public void clear() {
        menuRepo.deleteAll();
        mealRepo.deleteAll();
    }

    @Test
    public void getDishFromMenuSuccess() {
        //given
        //when
        List<Dish> dishes = menuService.getDishesFromMenu();
        MealEntity meal = new ArrayList<>(menuRepo.getBySeason(menuService.getCurrentSeason()).get().getMeals()).get(0);
        //then
        assertThat(dishes).hasSize(1);
        assertThat(meal.getName()).isEqualTo(dishes.get(0).getName());
    }

    @Test
    public void getAllMenusSuccess() {
        //given
        //when
        List<MenuView> menus = menuService.getAllMenus();
        //then
        assertThat(menus).hasSize(4);
        assertThat(menus.get(0).getMealMap()).hasSize(1);
        assertThat(menus.get(1).getMealMap()).hasSize(1);
        assertThat(menus.get(2).getMealMap()).hasSize(1);
        assertThat(menus.get(3).getMealMap()).hasSize(1);
    }

    @Test
    public void addMealToMenuByMealNameSuccess() {
        //given
        String season = "Lato";
        String mealName = "Filet z kurczaka";
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        MealMenu mealMenu = new MealMenu(mealName, menuId);
        //when
        MealShortView mealShortView = menuService.addMealToMenu(mealMenu);
        Set<String> mealsInMenu = menuRepo.getBySeason(season).get().getMeals().stream()
                .map(MealEntity::getName)
                .collect(Collectors.toSet());
        //then
        assertNotNull(mealShortView);
        assertThat(mealShortView.getName()).isEqualTo(mealName);
        assertTrue(mealsInMenu.contains(mealName));
    }

    @Test
    public void addMealToMenuByMealIdSuccess() {
        //given
        String season = "Lato";
        String mealName = "Filet z kurczaka";
        Integer mealId = mealRepo.findByName(mealName).get().getMealId();
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        MealMenu mealMenu = new MealMenu(String.valueOf(mealId), menuId);
        //when
        MealShortView mealShortView = menuService.addMealToMenu(mealMenu);
        Set<String> mealsInMenu = menuRepo.getBySeason(season).get().getMeals().stream()
                .map(MealEntity::getName)
                .collect(Collectors.toSet());
        //then
        assertNotNull(mealShortView);
        assertThat(mealShortView.getName()).isEqualTo(mealName);
        assertTrue(mealsInMenu.contains(mealName));
    }

    @Test
    public void addMealToMenuFailureMenuNotFound() {
        //given
        String mealName = "Filet z kurczaka";
        Integer menuId = 0;
        MealMenu mealMenu = new MealMenu(mealName, menuId);
        //when
        //then
        assertThrows(MenuNotFoundException.class, () -> menuService.addMealToMenu(mealMenu));
    }

    @Test
    public void addMealToMenuFailureInvalidMealNameOrId() {
        //given
        String season = "Lato";
        String mealName = String.valueOf(0);
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        MealMenu mealMenu = new MealMenu(mealName, menuId);
        //when
        //then
        assertThrows(MealNotFoundException.class, () -> menuService.addMealToMenu(mealMenu));
    }

    @Test
    public void addMealToMenuFailureMealAlreadyAdded() {
        //given
        String season = "Lato";
        String mealName = "Barszcz biały";
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        MealMenu mealMenu = new MealMenu(mealName, menuId);
        //when
        //then
        assertThrows(MealAlreadyAddedToMenuException.class, () -> menuService.addMealToMenu(mealMenu));
    }

    @Test
    public void removeMealFromMenuSuccess() {
        //given
        String season = "Lato";
        String mealName = "Barszcz biały";
        Integer mealId = mealRepo.findByName(mealName).get().getMealId();
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        //when
        menuService.removeMealFromMenu(menuId, mealId);
        Set<String> mealsInMenu = menuRepo.getBySeason(season).get().getMeals().stream()
                .map(MealEntity::getName)
                .collect(Collectors.toSet());
        //then
        assertThat(mealsInMenu).hasSize(0);
    }

    @Test
    public void removeMealFromMenuFailureMealNotFound() {
        //given
        String season = "Lato";
        Integer mealId = 0;
        Integer menuId = menuRepo.getBySeason(season).get().getMenuId();
        //when
        //then
        assertThrows(MealNotFoundException.class, () -> menuService.removeMealFromMenu(menuId, mealId));
    }

    @Test
    public void removeMealFromMenuFailureMenuNotFound() {
        //given
        String mealName = "Barszcz biały";
        Integer mealId = mealRepo.findByName(mealName).get().getMealId();
        Integer menuId = 0;
        //when
        //then
        assertThrows(MenuNotFoundException.class, () -> menuService.removeMealFromMenu(menuId, mealId));
    }

    private MenuEntity getMenu(String season, List<MealEntity> meals) {
        return new MenuEntity().builder()
                .season(season)
                .meals(new HashSet<>(meals))
                .build();
    }

    private MealEntity getMeal(String mealName, String typeName) {
        EasyRandom generator = new EasyRandom(getParameters());
        MealEntity meal = generator.nextObject(MealEntity.class);
        meal.setMealId(null);
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
