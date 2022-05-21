package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.supplyservice.data.entity.GoodEntity;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;

public interface GoodRepo extends JpaRepository<GoodEntity, Long> {
    @Modifying
    @Query("delete from GoodEntity g where g.invoice.invoiceNr = :nr")
    void deleteAllByInvoice(@Param("nr") String invoiceNr);
}
