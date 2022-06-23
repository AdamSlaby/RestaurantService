package pl.restaurant.menuservice.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.business.service.MealServiceImpl;
import pl.restaurant.menuservice.data.entity.IngredientEntity;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.TypeEntity;
import pl.restaurant.menuservice.data.entity.UnitEntity;
import pl.restaurant.menuservice.data.repository.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MealControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private TypeRepo typeRepo;

    @Autowired
    private IngredientRepo ingredientRepo;

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private MealIngredientRepo mealIngredientRepo;

    @LocalServerPort
    private int port;

    @Value("${app.file.path}")
    private String path;

    private URL url;

    @BeforeEach
    public void setUp() throws Exception {

        getUnit("kg");
        this.url = new URL("http://localhost:" + port + "/");
    }

    @AfterEach
    public void clear() {
        List<MealEntity> all = mealRepo.findAll();
        if (all.size() > 0) {
            MealEntity meal = all.get(0);
            if (meal.getImageUrl().contains(MealServiceImpl.IMAGE_URL))
                new File(this.path + meal.getImageUrl().split(MealServiceImpl.IMAGE_URL)[1]).deleteOnExit();
        }
        mealIngredientRepo.deleteAll();
        ingredientRepo.deleteAll();
        unitRepo.deleteAll();
        typeRepo.deleteAll();
        mealRepo.deleteAll();
    }

    @Test
    public void addMealSuccess() {
        //given
        String mealName = "Omlet";
        Meal meal = getMeal(mealName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "meal/"), entity, String.class);
        Optional<MealEntity> optMeal = mealRepo.findByName(mealName);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(optMeal.isPresent());
    }

    @Test
    public void addMealFailureMealAlreadyExists() {
        //given
        String mealName = "Omlet";
        mealRepo.save(getMealEntity(mealName, "Dania główne"));
        Meal meal = getMeal(mealName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "meal/"), entity, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addMealFailureTypeNotFound() {
        //given
        String mealName = "Omlet";
        Meal meal = getMeal(mealName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("typeId", 0);
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(URI.create(this.url + "meal/"), entity, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateMealSuccess() {
        //given
        String mealName = "Omlet";
        String updatedName = "Jajecznica";
        Integer mealId = mealRepo.save(getMealEntity(mealName, "Dania główne")).getMealId();
        Meal meal = getMeal(updatedName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "meal/" + mealId, HttpMethod.PATCH, entity, String.class);
        Optional<MealEntity> optMeal = mealRepo.findByName(updatedName);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(optMeal.isPresent());
    }

    @Test
    public void updateMealFailureMealAlreadyExists() {
        //given
        String mealName = "Omlet";
        String updatedName = "Jajecznica";
        mealRepo.save(getMealEntity(updatedName, "Dania główne"));
        Integer mealId = mealRepo.save(getMealEntity(mealName, "Dania główne")).getMealId();
        Meal meal = getMeal(updatedName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "meal/" + mealId, HttpMethod.PATCH, entity, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateMealFailureMealNotFound() {
        //given
        String mealName = "Omlet";
        String updatedName = "Jajecznica";
        mealRepo.save(getMealEntity(mealName, "Dania główne")).getMealId();
        Meal meal = getMeal(updatedName, "Dania główne", "Jajka");
        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        mapObjectToMap(body, meal);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "meal/" + -1, HttpMethod.PATCH, entity, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteMealSuccess() {
        //given
        String mealName = "Omlet";
        Integer mealId = mealRepo.save(getMealEntity(mealName, "Dania główne")).getMealId();
        //when
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "meal/" + mealId, HttpMethod.DELETE, null, String.class);
        Optional<MealEntity> optMeal = mealRepo.findByName(mealName);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFalse(optMeal.isPresent());
    }

    @Test
    public void deleteMealFailureMealNotFound() {
        //given
        Integer mealId = 0;
        //when
        ResponseEntity<String> response = restTemplate
                .exchange(this.url + "meal/" + mealId, HttpMethod.DELETE, null, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void mapObjectToMap(MultiValueMap<String, Object> body, Meal meal) {
        body.add("name", meal.getName());
        body.add("typeId", meal.getTypeId());
        body.add("price", meal.getPrice());
        body.add("image", getFile());
        body.add("ingredients", meal.getIngredients());
    }

    private MealEntity getMealEntity(String mealName, String typeName) {
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

    private Meal getMeal(String name, String type, String ingredient) {
        return new Meal().builder()
                .name(name)
                .typeId(getTypeEntity(type).getTypeId())
                .price(BigDecimal.valueOf(22.50))
                .image(null)
                .ingredients(getIngredient(ingredient))
                .build();
    }

    private ClassPathResource getFile() {
        return new ClassPathResource("test.jpg");
    }

    private String getIngredient(String ingredient) {
        try {
            return new ObjectMapper().writeValueAsString(Collections.singletonList(new Ingredient().builder()
                    .id(getIngredientEntity(ingredient).getIngredientId())
                    .name(ingredient)
                    .amount(BigDecimal.valueOf(40))
                    .unitId(getUnit("g").getUnitId())
                    .build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private IngredientEntity getIngredientEntity(String ingredient) {
        Optional<Integer> optIngredient = ingredientRepo.getIngredientByName(ingredient);
        return optIngredient.map(integer -> ingredientRepo.findById(integer).get())
                .orElseGet(() -> ingredientRepo.save(new IngredientEntity().builder()
                .name(ingredient)
                .build()));
    }

    private UnitEntity getUnit(String unit) {
        Optional<UnitEntity> optUnit = unitRepo.findByName(unit);
        return optUnit.orElseGet(() -> unitRepo.save(new UnitEntity().builder()
                .name(unit)
                .build()));
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
