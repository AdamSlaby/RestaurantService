package pl.restaurant.menuservice.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.menuservice.api.mapper.MealIngredientMapper;
import pl.restaurant.menuservice.api.mapper.MealMapper;
import pl.restaurant.menuservice.api.request.*;
import pl.restaurant.menuservice.api.response.*;
import pl.restaurant.menuservice.business.exception.ColumnNotFoundException;
import pl.restaurant.menuservice.business.exception.meal.CannotDeserializeIngredientsException;
import pl.restaurant.menuservice.business.exception.meal.MealAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.meal.MealNotFoundException;
import pl.restaurant.menuservice.business.exception.type.TypeNotFoundException;
import pl.restaurant.menuservice.business.utility.FileUtility;
import pl.restaurant.menuservice.business.utility.ImageValidator;
import pl.restaurant.menuservice.data.entity.MealEntity;
import pl.restaurant.menuservice.data.entity.MealIngredientEntity;
import pl.restaurant.menuservice.data.entity.TypeEntity;
import pl.restaurant.menuservice.data.repository.MealIngredientRepo;
import pl.restaurant.menuservice.data.repository.MealRepo;
import pl.restaurant.menuservice.data.repository.TypeRepo;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class MealServiceImpl implements MealService {
    private final static int AMOUNT = 10;
    private final static int BEST_MEALS_AMOUNT = 7;
    private static final String IMAGE_URL = "http://localhost:9000/menu/image/";

    @Value("${app.file.path}")
    private String path;
    private MealRepo mealRepo;
    private MealIngredientRepo mealIngredientRepo;
    private IngredientService ingredientService;
    private SupplyServiceClient supplyClient;
    private TypeRepo typeRepo;
    private ApplicationContext applicationContext;
    private MenuService menuService;

    public MealServiceImpl(MealRepo mealRepo, MealIngredientRepo mealIngredientRepo,
                           IngredientService ingredientService, SupplyServiceClient supplyClient,
                           TypeRepo typeRepo, ApplicationContext applicationContext, MenuService menuService) {
        this.mealRepo = mealRepo;
        this.mealIngredientRepo = mealIngredientRepo;
        this.ingredientService = ingredientService;
        this.supplyClient = supplyClient;
        this.typeRepo = typeRepo;
        this.applicationContext = applicationContext;
        this.menuService = menuService;
    }

    @Override
    public MealInfo getMealInfo(Integer mealId) {
        MealEntity mealEntity = mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
        List<MealIngredientEntity> ingredients = mealIngredientRepo.findByMeal(mealEntity);
        return MealMapper.mapDataToInfo(mealEntity, ingredients);
    }

    @Override
    public MealEntity getMeal(String name) {
        return mealRepo.findByName(name)
                .orElseThrow(MealNotFoundException::new);
    }

    @Override
    public MealEntity getMeal(Integer mealId) {
        return mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
    }

    @Override
    public List<DishListView> getAllMealsFromMenu() {
        return menuService.getAllDishesFromMenu();
    }

    @Override
    public MealListView getMeals(MealFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<MealShortInfo> page = mealRepo.getMeals(filters.getMealName(), filters.getTypeId(), pageable);
        return new MealListView(page.getTotalElements(), page.getContent());
    }

    @Override
    public List<Dish> getBestMeals() {
        Pageable pageable = PageRequest.of(0, BEST_MEALS_AMOUNT);
        List<MealEntity> meals = mealRepo.findAllByIsBest(true, pageable);
        return meals.stream()
                .map(MealMapper::mapDataToDish)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addMeal(Meal meal) {
        List<Ingredient> ingredients = deserializeStringToList(meal.getIngredients());
        if (mealRepo.existsByName(meal.getName()))
            throw new MealAlreadyExistsException();
        validateImage(meal.getImage());
        TypeEntity type = typeRepo.findById(meal.getTypeId())
                .orElseThrow(TypeNotFoundException::new);
        MealEntity mealEntity = mealRepo.save(MealMapper.mapObjectToData(meal, type, IMAGE_URL));
        for (int i = 0; i < ingredients.size(); i++)
            ingredientService.addIngredientsForMeal(ingredients, mealEntity, i);
        String filename = FileUtility.fileUpload(meal.getImage(), path);
        mealEntity.setImageUrl(IMAGE_URL + filename);
        mealRepo.save(mealEntity);
    }

    @Override
    public void updateMeal(Integer mealId, Meal meal) {
        List<Ingredient> ingredients = deserializeStringToList(meal.getIngredients());
        MealEntity mealEntity = mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
        if (mealRepo.existsByName(meal.getName()) && !mealEntity.getName().equals(meal.getName()))
            throw new MealAlreadyExistsException();
        getMealServiceProxy().removeIngredientsFromMeal(mealEntity);
        updateMeal(mealEntity, meal, ingredients);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMeal(MealEntity mealEntity, Meal meal, List<Ingredient> ingredients) {
        TypeEntity type = typeRepo.findById(meal.getTypeId())
                .orElseThrow(TypeNotFoundException::new);
        mealEntity.setName(meal.getName());
        mealEntity.setPrice(meal.getPrice());
        mealEntity.setType(type);
        for (int i = 0; i < ingredients.size(); i++)
            ingredientService.addIngredientsForMeal(ingredients, mealEntity, i);
        if (meal.getImage() != null)
            updateMealImage(meal, mealEntity);
        mealRepo.save(mealEntity);
    }

    @Transactional
    public void removeIngredientsFromMeal(MealEntity mealEntity) {
        mealIngredientRepo.deleteAllByMeal(mealEntity);
    }

    @Override
    @Transactional
    public void deleteMeal(Integer mealId) {
        MealEntity mealEntity = mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
        String[] parts = mealEntity.getImageUrl().split("/");
        FileUtility.deleteFile(parts[parts.length - 1], path);
        mealIngredientRepo.deleteAllByMeal(mealEntity);
        mealRepo.delete(mealEntity);
    }

    @Override
    public String validateOrders(Long restaurantId, List<Order> orders) {
        List<Integer> ids = orders.stream().map(Order::getDishId).collect(Collectors.toList());
        Map<Integer, MealEntity> meals = mealRepo.getAllByMealIdIn(ids).stream()
                .collect(Collectors.toMap(MealEntity::getMealId, v -> v));
        List<OrderValidation> orderList = new ArrayList<>();
        if (meals.size() != orders.size())
            return "Posiłek o podanym identyfikatorze nie istnieje";
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            MealEntity mealEntity = meals.get(order.getDishId());
            if (mealEntity.getPrice().multiply(BigDecimal.valueOf(order.getAmount())).compareTo(order.getPrice()) != 0)
                return "Nieprawidłowa cena posiłku";
            orderList.add(new OrderValidation().builder()
                    .dishId(order.getDishId())
                    .name(mealEntity.getName())
                    .amount(order.getAmount())
                    .ingredients(mealEntity.getIngredients().stream()
                            .map(MealIngredientMapper::mapDataToView)
                            .collect(Collectors.toList()))
                    .build());
        }
        supplyClient.updateSupplies(restaurantId, orderList);
        return null;
    }

    @Override
    public void validateOrder(Long restaurantId, Order order) {
        MealEntity mealEntity = mealRepo.getByMealId(order.getDishId())
                .orElseThrow(MealNotFoundException::new);
        OrderValidation orderValidation = new OrderValidation().builder()
                .dishId(order.getDishId())
                .name(mealEntity.getName())
                .amount(order.getAmount())
                .ingredients(mealEntity.getIngredients().stream()
                        .map(MealIngredientMapper::mapDataToView)
                        .collect(Collectors.toList()))
                .build();
        supplyClient.checkSupplies(restaurantId, orderValidation);
    }

    @Override
    public void rollbackOrderSupplies(Long restaurantId, List<Order> orders) {
        List<Integer> ids = orders.stream().map(Order::getDishId).collect(Collectors.toList());
        List<MealEntity> meals = mealRepo.getAllByMealIdIn(ids);
        List<OrderValidation> orderList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            MealEntity mealEntity = meals.get(i);
            orderList.add(new OrderValidation().builder()
                    .dishId(order.getDishId())
                    .name(mealEntity.getName())
                    .amount(order.getAmount())
                    .ingredients(mealEntity.getIngredients().stream()
                            .map(MealIngredientMapper::mapDataToView)
                            .collect(Collectors.toList()))
                    .build());
        }
        supplyClient.rollbackOrderSupplies(restaurantId, orderList);
    }


    private Pageable mapSortEventToPageable(MealFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                MealEntity.class.getDeclaredField(column);
                if (column.equals("type"))
                    column = column + ".name";
                if (filters.getSortEvent().getDirection().equals(SortDirection.ASC))
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.ASC, column));
                else if (filters.getSortEvent().getDirection().equals(SortDirection.DESC)) {
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(Sort.Direction.DESC, column));
                } else
                    return PageRequest.of(filters.getPageNr(), AMOUNT, Sort.by(column));
            } catch (NoSuchFieldException e) {
                throw new ColumnNotFoundException();
            }
        }
    }

    private MealServiceImpl getMealServiceProxy() {
        return applicationContext.getBean(this.getClass());
    }

    private void updateMealImage(Meal meal, MealEntity mealEntity) {
        validateImage(meal.getImage());
        String filename = FileUtility.fileUpload(meal.getImage(), path);
        String[] parts = mealEntity.getImageUrl().split("/");
        FileUtility.deleteFile(parts[parts.length - 1], path);
        mealEntity.setImageUrl(IMAGE_URL + filename);
    }

    private void validateImage(MultipartFile image) {
        ImageValidator.validateImageNull(image);
        ImageValidator.validateImageExtension(image);
        ImageValidator.validateImageSize(image);
    }

    private List<Ingredient> deserializeStringToList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Ingredient> ingredients = Arrays.asList(mapper.readValue(json, Ingredient[].class));
            return ingredients;
        } catch (JsonProcessingException e) {
            throw new CannotDeserializeIngredientsException();
        }
    }
}
