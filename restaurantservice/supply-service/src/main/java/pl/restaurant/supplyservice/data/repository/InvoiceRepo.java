package pl.restaurant.supplyservice.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.restaurant.supplyservice.api.response.InvoiceShortInfo;
import pl.restaurant.supplyservice.data.entity.InvoiceEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<InvoiceEntity, String> {
    @EntityGraph(attributePaths = {"goods", "goods.unit", "sellerAddress", "buyerAddress"})
    Optional<InvoiceEntity> getByInvoiceNr(String nr);

    @Query("select new pl.restaurant.supplyservice.api.response." +
            "InvoiceShortInfo(i.invoiceNr, i.date, i.sellerName, i.price) " +
            "from InvoiceEntity i " +
            "where (:rId is null or i.restaurantId = :rId) and " +
            "(:date is null or i.date = :date) and " +
            "(:nr is null or i.invoiceNr like %:nr%) and " +
            "(:name is null or i.sellerName like %:name%)")
    Page<InvoiceShortInfo> getInvoiceList(@Param("nr") String nr,
                                          @Param("date") LocalDate date,
                                          @Param("name") String sellerName,
                                          @Param("rId") Long restaurantId,
                                          Pageable pageable);

    boolean existsByInvoiceNr(String nr);
}
