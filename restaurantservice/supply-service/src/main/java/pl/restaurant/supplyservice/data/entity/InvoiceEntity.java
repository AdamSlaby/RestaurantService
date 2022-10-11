package pl.restaurant.supplyservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Invoice")
public class InvoiceEntity implements Serializable {
    private static final long serialVersionUID = 4710359136505953549L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(length = 20)
    private String invoiceNr;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 67, nullable = false)
    private String sellerName;

    @Column(length = 67, nullable = false)
    private String buyerName;

    @Column(length = 11, nullable = false)
    private String sellerNip;

    @Column(length = 11, nullable = false)
    private String buyerNip;

    @Column(nullable = false)
    private LocalDate completionDate;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<GoodEntity> goods;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_address_id", nullable = false)
    private AddressEntity sellerAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_address_id", nullable = false)
    private AddressEntity buyerAddress;
}
