package pl.restaurant.supplyservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.restaurant.supplyservice.api.request.TaxType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Good")
public class GoodEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodId;

    @Column(nullable = false)
    private Integer ingredientId;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal unitNetPrice;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal discount;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal netPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxType taxType;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal taxPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitEntity unit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_nr", nullable = false)
    private InvoiceEntity invoice;
}
