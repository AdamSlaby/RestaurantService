package pl.restaurant.orderservice.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class IncorrectDataAdvice {
    @ResponseBody
    @ExceptionHandler(CannotCompleteOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> cannotCompleteOrderHandler(CannotCompleteOrderException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("orderId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(CannotUpdateOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> cannotUpdateOrderHandler(CannotUpdateOrderException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("orderId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(RestaurantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> restaurantNotFoundHandler(RestaurantNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("restaurantId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(SupplyErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> mealAlreadyAddedToMenuHandler(SupplyErrorException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("order", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(TableNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> tableNotFoundHandler(TableNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("tableId", ex.getMessage());
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
    @ExceptionHandler(PaypalErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> paypalErrorHandler(PaypalErrorException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("paypal", ex.getMessage());
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
