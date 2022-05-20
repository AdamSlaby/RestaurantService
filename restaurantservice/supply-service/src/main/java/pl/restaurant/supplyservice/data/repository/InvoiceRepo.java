package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;

public interface InvoiceRepo extends JpaRepository<InvoiceEntity, String> {
}
