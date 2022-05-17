package pl.restaurant.menuservice.business.exception;

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
            String fieldName = "Name";
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
