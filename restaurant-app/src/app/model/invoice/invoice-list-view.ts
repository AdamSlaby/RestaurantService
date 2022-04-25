import {InvoiceShortInfo} from "./invoice-short-info";

export interface InvoiceListView {
  totalElements: number;
  invoices: InvoiceShortInfo[];
}
