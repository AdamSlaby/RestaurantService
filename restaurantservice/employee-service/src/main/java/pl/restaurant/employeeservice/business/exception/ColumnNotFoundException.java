package pl.restaurant.employeeservice.business.exception;

import org.springframework.data.jpa.repository.JpaRepository;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException() {
        super("Podana kolumna do sortowania nie istnieje");
    }
}
