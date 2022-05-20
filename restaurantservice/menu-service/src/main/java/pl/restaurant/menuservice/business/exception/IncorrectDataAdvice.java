package pl.restaurant.menuservice.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.restaurant.menuservice.business.exception.image.CannotSaveImageException;
import pl.restaurant.menuservice.business.exception.image.IncorrectImageException;
import pl.restaurant.menuservice.business.exception.image.IncorrectImageExtensionException;
import pl.restaurant.menuservice.business.exception.image.InvalidImageSizeException;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.ingredient.IngredientNotFoundException;
import pl.restaurant.menuservice.business.exception.meal.CannotDeserializeIngredientsException;
import pl.restaurant.menuservice.business.exception.meal.MealAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.meal.MealNotFoundException;
import pl.restaurant.menuservice.business.exception.menu.MenuNotFoundException;
import pl.restaurant.menuservice.business.exception.type.TypeAlreadyExistsException;
import pl.restaurant.menuservice.business.exception.type.TypeNotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class IncorrectDataAdvice {
    @ResponseBody
    @ExceptionHandler(CannotDeserializeIngredientsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> cannotDeserializeIngredientsHandler(CannotDeserializeIngredientsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ingredients[0].name", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(MenuNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> menuNotFoundHandler(MenuNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("menuId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(IngredientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> ingredientNotFoundHandler(IngredientNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        int i = Integer.parseInt(ex.getMessage().split(" ")[0]);
        errors.put("ingredient[" + i + "].name", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(UnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> unitNotFoundHandler(UnitNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        int i = Integer.parseInt(ex.getMessage().split(" ")[0]);
        errors.put("ingredient[" + i + "].unit", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(IngredientAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> ingredientAlreadyExistsHandler(IngredientAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        int i = Integer.parseInt(ex.getMessage().split(" ")[0]);
        errors.put("ingredient[" + i + "].name", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(MealAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> mealAlreadyExistsHandler(MealAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("name", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(MealNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> mealNotFoundHandler(MealNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("meal", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(ColumnNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> columnNotFoundHandler(ColumnNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("column", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(TypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> typeNotFoundHandler(TypeNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("typeId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(TypeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> typeAlreadyExistsHandler(TypeAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("type", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(CannotSaveImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> cannotSaveImageHandler(CannotSaveImageException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(InvalidImageSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidImageSizeHandler(InvalidImageSizeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(IncorrectImageExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> incorrectImageExtensionHandler(IncorrectImageExtensionException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(IncorrectImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> incorrectImageHandler(IncorrectImageException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("image", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, String> handleValidationExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String[] parts = error.getPropertyPath().toString().split("\\.");
            String fieldName;
            StringBuilder builder = new StringBuilder();
            if (parts.length > 1) {
                for (int i = 1; i < parts.length; i++)
                    builder.append(parts[i]).append(".");
                builder.deleteCharAt(builder.length() - 1);
                fieldName = builder.toString();
            } else
                fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
