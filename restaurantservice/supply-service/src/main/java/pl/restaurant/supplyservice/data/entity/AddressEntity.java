package pl.restaurant.supplyservice.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Address")
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = -413341433841359850L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(length = 60, nullable = false)
    private String city;

    @Column(length = 60, nullable = false)
    private String street;

    @Column(length = 6, nullable = false)
    private String houseNr;

    @Column(length = 4, nullable = false)
    private String flatNr;

    @Column(length = 7, nullable = false)
    private String postcode;

    @OneToOne(mappedBy = "sellerAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private InvoiceEntity sellerInvoice;

    @OneToOne(mappedBy = "buyerAddress", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private InvoiceEntity buyerInvoice;
}
