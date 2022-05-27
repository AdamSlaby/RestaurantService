package pl.restaurant.supplyservice.business.exception;

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
    @ExceptionHandler(SupplyErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String supplyAlreadyExistsHandler(SupplyErrorException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SupplyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> supplyAlreadyExistsHandler(SupplyAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ingredientName", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(IngredientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> ingredientNotFoundHandler(IngredientNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ingredientId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(UnitMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> unitMismatchHandler(UnitMismatchException ex) {
        return constructMapForPartError(ex);
    }
    @ResponseBody
    @ExceptionHandler(SupplyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> supplyNotFoundHandler(SupplyNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("supplyId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(UnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> unitNotFoundHandler(UnitNotFoundException ex) {
        return constructMapForPartError(ex);
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
    @ExceptionHandler(InvoiceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invoiceAlreadyExistsHandler(InvoiceAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("nr", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(InvalidNipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidNipHandler(InvalidNipException ex) {
        Map<String, String> errors = new HashMap<>();
        String[] errorParts = ex.getMessage().split("\\.");
        if (errorParts.length > 1)
            errors.put(errorParts[0], errorParts[1]);
        else
            errors.put("nip", ex.getMessage());
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
    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> invoiceNotFoundHandler(InvoiceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("invoiceNr", ex.getMessage());
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

    private Map<String, String> constructMapForPartError(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        String[] errorParts = ex.getMessage().split("_");
        if (errorParts.length > 1)
            errors.put(errorParts[0], errorParts[1]);
        else
            errors.put("unitId", ex.getMessage());
        return errors;
    }
}
