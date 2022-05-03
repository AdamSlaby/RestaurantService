package pl.restaurant.employeeservice.business.exception;

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
public class EmployeeIncorrectDataAdvice {
    @ResponseBody
    @ExceptionHandler(WorkstationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> invalidAccountNrHandler(WorkstationNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("workstationId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(InvalidAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidAccountNrHandler(InvalidAccountException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("accountNr", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(InvalidPeselException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidPeselHandler(InvalidPeselException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("pesel", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(ScheduleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> scheduleNotFoundHandler(ScheduleNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("scheduleId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(RestaurantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> columnNotFoundHandler(RestaurantNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("restaurantId", ex.getMessage());
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> columnNotFoundHandler(EmployeeNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("employeeId", ex.getMessage());
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
