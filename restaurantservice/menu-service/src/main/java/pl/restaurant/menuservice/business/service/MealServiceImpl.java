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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import pl.restaurant.menuservice.api.mapper.MealIngredientMapper;
import pl.restaurant.menuservice.api.mapper.MealMapper;
import pl.restaurant.menuservice.api.request.Ingredient;
import pl.restaurant.menuservice.api.request.Meal;
import pl.restaurant.menuservice.api.request.MealFilters;
import pl.restaurant.menuservice.api.request.SortDirection;
import pl.restaurant.menuservice.api.response.MealInfo;
import pl.restaurant.menuservice.api.response.MealListView;
import pl.restaurant.menuservice.api.response.MealShortInfo;
import pl.restaurant.menuservice.business.exception.ColumnNotFoundException;
import pl.restaurant.menuservice.business.exception.meal.CannotDeserializeIngredientsException;
import pl.restaurant.menuservice.business.exception.meal.MealAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.meal.MealNotFoundException;
import pl.restaurant.menuservice.business.exception.type.TypeNotFoundException;
import pl.restaurant.menuservice.business.utility.FileUtility;
import pl.restaurant.menuservice.business.utility.ImageValidator;
import pl.restaurant.menuservice.data.entity.*;
import pl.restaurant.menuservice.data.repository.MealIngredientRepo;
import pl.restaurant.menuservice.data.repository.MealRepo;
import pl.restaurant.menuservice.data.repository.TypeRepo;
import pl.restaurant.menuservice.data.repository.UnitRepo;

import javax.persistence.PreRemove;
import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Arrays;
import java.util.List;

@Service
@Validated
public class MealServiceImpl implements MealService {
    private final static int AMOUNT = 10;
    private static final String IMAGE_URL = "http://localhost:9000/menu/image/";

    @Value("${app.file.path}")
    private String path;
    private MealRepo mealRepo;
    private MealIngredientRepo mealIngredientRepo;
    private IngredientService ingredientService;
    private TypeRepo typeRepo;
    private ApplicationContext applicationContext;

    public MealServiceImpl(MealRepo mealRepo, MealIngredientRepo mealIngredientRepo,
                           IngredientService ingredientService, TypeRepo typeRepo,
                           ApplicationContext applicationContext) {
        this.mealRepo = mealRepo;
        this.mealIngredientRepo = mealIngredientRepo;
        this.ingredientService = ingredientService;
        this.typeRepo = typeRepo;
        this.applicationContext = applicationContext;
    }

    @Override
    public MealInfo getMealInfo(Integer mealId) {
        MealEntity mealEntity = mealRepo.findById(mealId)
                .orElseThrow(MealNotFoundException::new);
        return MealMapper.mapDataToInfo(mealEntity);
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
    public MealListView getMeals(MealFilters filters) {
        Pageable pageable = mapSortEventToPageable(filters);
        Page<MealShortInfo> page = mealRepo.getMeals(filters.getMealName(), filters.getTypeId(), pageable);
        return new MealListView(page.getTotalElements(), page.getContent());
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
        getSpringProxy().removeIngredientsFromMeal(mealEntity);
        updateMeal(mealEntity, meal, ingredients);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
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


    private Pageable mapSortEventToPageable(MealFilters filters) {
        if (filters.getSortEvent() == null) {
            return PageRequest.of(filters.getPageNr(), AMOUNT);
        } else {
            try {
                String column = filters.getSortEvent().getColumn();
                MealEntity.class.getDeclaredField(column);
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void removeIngredientsFromMeal(MealEntity mealEntity) {
        mealIngredientRepo.deleteAllByMeal(mealEntity);
    }

    private MealServiceImpl getSpringProxy() {
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
