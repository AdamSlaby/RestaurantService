package pl.restaurant.supplyservice.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListView {
    private Long totalElements;
    private List<InvoiceShortInfo> invoices;
}
