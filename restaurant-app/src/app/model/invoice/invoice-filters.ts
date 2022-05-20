import { SortEvent } from "../sort-event";

export interface InvoiceFilters {
    nr: any;
    date: any;
    sellerName: any;
    sortEvent: SortEvent;
    pageNr: number;
}